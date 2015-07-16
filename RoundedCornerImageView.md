# Introduction #

![http://android-batsg.googlecode.com/svn/trunk/docs/images/RoundedCornerImageViewDemo.png](http://android-batsg.googlecode.com/svn/trunk/docs/images/RoundedCornerImageViewDemo.png)

You have an ImageView with rounded corner by using [RoundedCornerImageView](http://code.google.com/p/android-batsg/source/browse/android-batsg/src/com/chauhai/android/batsg/widget/RoundedCornerImageView.java) view.


# Details #

Just set the attribute cornerRadius of the view like this.
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res/com.chauhai.android.batsg.demo"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:background="#ffffff"
    android:orientation="vertical"
    android:gravity="center">
    <com.chauhai.android.batsg.widget.RoundedCornerImageView
        android:layout_width="200dp"
        android:layout_height="150dp"
        android:src="@drawable/picture"
        android:scaleType="fitCenter"
        app:cornerRadius="10dp"
        />
</LinearLayout>
```

Please see the sample code in [android-batsg-demo](http://code.google.com/p/android-batsg/source/browse/android-batsg-demo/src/com/chauhai/android/batsg/demo/widget/RoundedCornerImageViewDemoActivity.java)