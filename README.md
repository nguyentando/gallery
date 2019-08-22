# gallery
**Functionalities:**
- Home screen with a **grid of square photos** pulling from https://api.unsplash.com/photos
- The home screen has **pagination**. When user scroll down, the app will keep loading data from the remote source until there is no more data.
- **Pull to refresh**.
- Tap on a photo should present a new view with the photo **full-screen**.
- **Automatically switch between different urls to balance between loading time and quality:** The unsplash API provides 2 urls for each photo: `thumbnail` and `full`. The app will use `thumbnail url` to show the photo in the home screen in `low quality` to improve the loading time. When the details screen is shown, the app will first show the `thumbnail url`, then automatically switch to the `full url` to show the photo in `higher quality`.
- **Long press on a photo** should show options to **share** (in either the home screen or the details screen).
- The app works **offline (using cache)**.
- Having **unit tests**.

**Tech stack:**
- Architecture: **Clean-MVVM Architecture.**
- Language: **100% Kotlin.**
- Android Architecture Components: **Lifecycles, LiveData, ViewModel, Paging, Room.**
- Database: **Room.**
- Network: **Retrofit.**
- Image: **Glide.**
- Reactive programming: **RxJava2, RxKotlin, RxAndroid, LiveData.**
- Dependency injection: **Dagger 2.**
- Serialization/Deserialization: **gson.**
- Unit testing: **JUnit.**
- Mocking: **Mockito.**
- UI testing: **Espresso, Espresso Test Recorder** (I haven't had time to implement UI tests yet).

**Requirements:**
- Android Studio 3.5
- Java 8.
