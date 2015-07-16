# Introduction #

If you just want a splash image to be display for a while when the application is started, then you can create it easily using [BaseSplashActivity class](http://code.google.com/p/android-batsg/source/browse/android-batsg/src/com/chauhai/android/batsg/activity/BaseSplashActivity.java).


# Details #

I have implemented splash screen by creating a splash layout xml, then set it as the content view of the activity, wait for a while and set the content view to another layout.

But there are many problems to solve when do thing this way: use which layout in onResume(), do not process some user action (touch or menu button for example) while in the splash screen etc.

So I think the good way is to implement the splash screen as an activity.

By extending the [BaseSplashActivity class](http://code.google.com/p/android-batsg/source/browse/android-batsg/src/com/chauhai/android/batsg/activity/BaseSplashActivity.java), you can create a splash screen easily. There is sample code in the document of the class.