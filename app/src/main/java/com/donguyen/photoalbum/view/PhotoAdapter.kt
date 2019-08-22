package com.donguyen.photoalbum.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.donguyen.photoalbum.R
import com.donguyen.photoalbum.model.Photo
import com.donguyen.photoalbum.util.GlideApp
import com.donguyen.photoalbum.view.PhotosAdapter.PhotoViewHolder

/**
 * Created by DoNguyen on 22/8/19.
 */
class PhotosAdapter(private val listener: OnItemListener? = null) :
    PagedListAdapter<Photo, PhotoViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position) ?: return
        holder.bind(photo, listener)
    }

    override fun onViewRecycled(holder: PhotoViewHolder) {
        super.onViewRecycled(holder)
        holder.clear()
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.id.hashCode().toLong()
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {

            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }
        }
    }

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val photoImg: ImageView = view.findViewById(R.id.photo_img)

        fun bind(photo: Photo, listener: OnItemListener?) {
            GlideApp.with(photoImg.context)
                .load(photo.thumbUrl)
                .placeholder(R.color.gray)
                .centerCrop()
                .into(photoImg)

            photoImg.setOnClickListener {
                listener?.onItemClicked(photoImg, photo)
            }

            photoImg.setOnLongClickListener {
                listener?.onItemLongClicked(photoImg, photo)
                true
            }
        }

        fun clear() {
            // release the drawable reference so it can be GC
            photoImg.setImageDrawable(null)
        }
    }

    interface OnItemListener {
        fun onItemClicked(imageView: ImageView, photo: Photo)
        fun onItemLongClicked(imageView: ImageView, photo: Photo)
    }
}