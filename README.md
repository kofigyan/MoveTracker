MoveTracker App
===========================================================

This app allows users to track their movements as locations.


Introduction
-------------

### Functionality
The app is composed of 2 main screens.
#### MainActivity
Allows the user to start and stop tracking of movement.
The app allows the user to turn-off the screen while tracking and this does 
not affect the functionality of the app. Tracking is ignored for distances  
between previous and current locations less than 1 meter.
 

#### AllEventsActivity
Show a list of all past traking events.
 

### Building
You can open the project in Android studio and press run.
### Testing
Still in development. 
The project will uses both instrumentation tests that run on the device
and local unit tests that run on your computer. 

#### Device Tests
##### UI Tests
The projects will use Espresso for UI testing.  

#### Local Unit Tests 


### Libraries
* [Android Extension Libraries][extension-lib]
* [Android Architecture Components][arch]
* [Retrofit][retrofit] for REST api communication
* [Glide][glide] for image loading
* [espresso][espresso] for UI tests
* [mockito][mockito] for mocking in tests
* [threetenabp][threetenabp] for timezone format
* [FusedLocationProviderClient][fuse] fuse location provider
* [static map][map] static map api


[extension-lib]: https://developer.android.com/jetpack/androidx
[arch]: https://developer.android.com/topic/libraries/architecture
[espresso]: https://google.github.io/android-testing-support-library/docs/espresso/
[retrofit]: http://square.github.io/retrofit
[glide]: https://github.com/bumptech/glide
[mockito]: http://site.mockito.org
[threetenabp]: https://github.com/JakeWharton/ThreeTenABP
[fuse]: https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient
[map]: https://developers.google.com/maps/documentation/maps-static/intro

 
