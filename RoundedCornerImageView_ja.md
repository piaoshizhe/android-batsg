# Introduction #

![http://android-batsg.googlecode.com/svn/trunk/docs/images/RoundedCornerImageViewDemo.png](http://android-batsg.googlecode.com/svn/trunk/docs/images/RoundedCornerImageViewDemo.png)

[RoundedCornerImageView](http://code.google.com/p/android-batsg/source/browse/android-batsg/src/com/chauhai/android/batsg/widget/RoundedCornerImageView.java) を使用することで、角が丸まるImageViewになる.


# Details #

次のように、cornerRadius属性を指定するだけです.
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

サンプルコードの詳細は、[android-batsg-demo](http://code.google.com/p/android-batsg/source/browse/android-batsg-demo/src/com/chauhai/android/batsg/demo/widget/RoundedCornerImageViewDemoActivity.java)でご覧ください.