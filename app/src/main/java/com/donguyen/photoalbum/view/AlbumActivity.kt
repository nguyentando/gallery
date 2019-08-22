package com.donguyen.photoalbum.view

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.donguyen.photoalbum.R
import com.donguyen.photoalbum.base.BaseActivity
import com.donguyen.photoalbum.model.Photo
import com.donguyen.photoalbum.util.GlideApp
import com.donguyen.photoalbum.util.extension.albumApplication
import com.donguyen.photoalbum.util.extension.show
import com.donguyen.photoalbum.util.extension.toast
import com.donguyen.photoalbum.viewmodel.AlbumVMFactory
import com.donguyen.photoalbum.viewmodel.AlbumViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_album.*
import javax.inject.Inject


/**
 * Created by DoNguyen on 9/3/19.
 */
class AlbumActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener, PhotosAdapter.OnItemListener {

    private val REQUEST_WRITE_EXTERNAL_STORAGE = 1

    // view model
    @Inject
    lateinit var factory: AlbumVMFactory
    private lateinit var viewModel: AlbumViewModel

    private lateinit var photosAdapter: PhotosAdapter

    private var currentFullPhoto: Photo? = null

    // for zoom animation
    private var currentAnimator: Animator? = null
    private var shortAnimationDuration: Int = 0

    // for share feature
    private var sharingPhoto: Photo? = null
    private var shareDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        albumApplication.createAlbumComponent().inject(this)
        viewModel = ViewModelProviders.of(this, factory).get(AlbumViewModel::class.java)

        initViews()
        observeViewState()
        viewModel.loadPhotos(refresh = false)

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    override fun onStop() {
        super.onStop()
        shareDisposable?.dispose()
    }

    override fun onBackPressed() {
        if (photo_full_img.isVisible) {
            currentAnimator?.cancel()
            currentAnimator?.start()
        } else {
            super.onBackPressed()
        }
    }

    private fun initViews() {
        swipe_refresh_layout.setOnRefreshListener(this)

        photosAdapter = PhotosAdapter(this).apply {
            setHasStableIds(true)
        }

        recycler_view.apply {
            layoutManager = GridLayoutManager(this@AlbumActivity, 3)
            adapter = photosAdapter
        }

        photo_full_img.setOnLongClickListener {
            if (currentFullPhoto != null) {
                sharePhoto(currentFullPhoto!!)
            }
            true
        }
    }

    private fun observeViewState() {
        viewModel.viewState.observe(this, Observer { viewState ->
            handleViewState(viewState)
        })
    }

    private fun handleViewState(state: AlbumViewState?) {
        state ?: return
        photosAdapter.submitList(state.photoList)

        // empty
        val isEmpty = state.photoList?.isEmpty() ?: false
        empty_txt.show(isEmpty)
        recycler_view.show(!isEmpty)

        // loading
        swipe_refresh_layout.isRefreshing = state.loading

        // error
        if (state.error.isNotEmpty()) {
            toast(state.error)
        }
    }

    override fun onRefresh() {
        viewModel.loadPhotos(refresh = true)
    }

    override fun onItemClicked(imageView: ImageView, photo: Photo) {
        zoomImageFromThumb(imageView, photo)
    }

    override fun onItemLongClicked(imageView: ImageView, photo: Photo) {
        sharePhoto(photo)
    }

    private fun sharePhoto(photo: Photo) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL_STORAGE
            )
            sharingPhoto = photo
            return
        }

        sharingPhoto = null
        shareDisposable?.dispose()
        shareDisposable = Observable
            .fromCallable {
                // try to get the full photo
                GlideApp.with(this).asFile().load(photo.fullUrl).submit().get()
            }
            .onErrorReturn {
                // fallback to the thumb photo
                GlideApp.with(this).asFile().load(photo.thumbUrl).submit().get()
            }
            .map { file ->
                // map from file to path
                MediaStore.Images.Media.insertImage(
                    contentResolver,
                    file.path, null, null
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { path ->
                    val uri = Uri.parse(path)
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "image/*"
                        putExtra(Intent.EXTRA_STREAM, uri)
                    }
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_this_photo_to)))
                }, {
                    toast(R.string.error_the_photo_is_not_available)
                })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sharePhoto(sharingPhoto!!)
                } else {
                    sharingPhoto = null
                }
                return
            }
        }
    }

    // TODO can use a separate fragment to show the full-resolution photo. Just keep it simple for now
    private fun zoomImageFromThumb(thumbView: ImageView, photo: Photo) {
        currentFullPhoto = photo

        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        currentAnimator?.cancel()

        // Load the thumb photo for the photo_full_img.
        GlideApp.with(this)
            .load(photo.thumbUrl)
            .placeholder(thumbView.drawable)
            .into(photo_full_img)

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        // TODO calculate the exact position of the photo_full_img on the screen (based the photo width and height -> we have photo ratio)
        // Then we zoom from thumbView to the photo_full_img instead of its container -> the zoom animation will look better
        thumbView.getGlobalVisibleRect(startBoundsInt)
        container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.alpha = 0f
        photo_full_img.show()

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        photo_full_img.pivotX = 0f
        photo_full_img.pivotY = 0f

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    photo_full_img,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(ObjectAnimator.ofFloat(photo_full_img, View.Y, startBounds.top, finalBounds.top))
                with(ObjectAnimator.ofFloat(photo_full_img, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(photo_full_img, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    // load the full resolution photo
                    GlideApp.with(this@AlbumActivity)
                        .load(photo.fullUrl)
                        .placeholder(photo_full_img.drawable)
                        .into(photo_full_img)

                    // Animate the four positioning/sizing properties in parallel,
                    // back to their original values.
                    currentAnimator = AnimatorSet().apply {
                        play(ObjectAnimator.ofFloat(photo_full_img, View.X, startBounds.left)).apply {
                            with(ObjectAnimator.ofFloat(photo_full_img, View.Y, startBounds.top))
                            with(ObjectAnimator.ofFloat(photo_full_img, View.SCALE_X, startScale))
                            with(ObjectAnimator.ofFloat(photo_full_img, View.SCALE_Y, startScale))
                        }
                        duration = shortAnimationDuration.toLong()
                        interpolator = DecelerateInterpolator()
                        addListener(object : AnimatorListenerAdapter() {

                            override fun onAnimationEnd(animation: Animator) {
                                thumbView.alpha = 1f
                                photo_full_img.visibility = View.GONE
                                currentAnimator = null
                                currentFullPhoto = null
                            }

                            override fun onAnimationCancel(animation: Animator) {
                                thumbView.alpha = 1f
                                photo_full_img.visibility = View.GONE
                                currentAnimator = null
                                currentFullPhoto = null
                            }
                        })
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }
    }
}
