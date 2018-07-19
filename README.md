# Pencake

**Platform: Android (Min SDK: 19, Target SDK: 27)**

**Author: [Timotius Oktorio](https://ca.linkedin.com/in/timotiusoktorio "LinkedIn Profile") (toktorio@gmail.com)**

This is a cake ordering app for a fictional bakery business called Pencake. Pencake is a local bakery shop in Mississauga that sells a variety of butter-cream cakes, cupcakes, cookies, and pastries.

Users can use this app to browse all available cakes, make orders, and find information about the business. Since this is just a fictional bakery business, orders will not be processed and no payments will be accepted. 

*This app is created as the capstone project of [Android Developer Nanodegree](https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801) program at Udacity.*

### Features
- Browse all available cakes and products offered by the bakery.
- Make orders, check order statuses, and see past orders.
- Save products that you like as a favorite for faster search.
- Find information about the bakery such as the location and contact details.
- Synchronize your data with all of your devices by creating an account.

### What I have learned from this project
- Planning and designing an app by creating a [project proposal](https://docs.google.com/document/d/1TD7zecP-zy1By4kKYzhgaODuWo82AX75aho6cWg37x8/edit?usp=sharing) and wireframes.
- Using [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/) libraries and [MVVM](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) software architectural pattern.
- Using [Firebase Realtime Database](https://firebase.google.com/docs/database/) to store data locally and remotely.
- Using [Firebase Authentication](https://firebase.google.com/docs/auth/) to authenticate user to the app.
- Using [Google Maps SDK for Android](https://developers.google.com/maps/documentation/android-sdk/intro) to display a map in the app.

### Google Maps API key configuration
This project uses Google Maps SDK for Android and requires an API key to use. For security purposes, the API key is hidden from the project. To use this project, you need to create your own API key and add it to the project.

When this project is opened the first time, there will be Gradle sync error because the project can not find the API key. To fix this issue, follow these steps:

1. [Get an API key from the Google Cloud Platform Console](https://developers.google.com/maps/documentation/android-sdk/signup).
2. Add this line to the gradle.properties file:
> Pencake_GoogleMapsApiKey="YOUR_API_KEY"
3. Replace YOUR_API_KEY with your API key.
4. Sync the project and the error will be resolved.

<br><img src="screenshots/screenshot_1.png" width="360" height="640" /> <img src="screenshots/screenshot_2.png" width="360" height="640" />