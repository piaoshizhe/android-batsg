# Introduction #

![http://android-batsg.googlecode.com/svn/trunk/docs/images/ProportionalLayoutDemo.png](http://android-batsg.googlecode.com/svn/trunk/docs/images/ProportionalLayoutDemo.png)

If you want to have a View (or ViewGroup) that has width/height is a fixed rate, [ProportionalLayout](http://code.google.com/p/android-batsg/source/browse/android-batsg/src/com/chauhai/android/batsg/widget/ProportionalLayout.java) will help.

# Details #

The example layout below seems to be long, but in fact, you just specify the **proportion** parameter of the ProportionalLayout, make its width (or height) computable, and leave the other side to 0.
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res/com.chauhai.android.batsg.demo"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="vertical" >
    <TextView
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="I want to create the View 2 that has width/height = 2/3, and takes 40% of the parent's width. So the parent (ProportionalLayout) has width/height = 1 / (0.4 * 1.5) = 1.6(6). The width is specified as fill_parent, and the height is adjusted based on it"
        />
    <com.chauhai.android.batsg.widget.ProportionalLayout
        android:layout_width="fill_parent"
        android:layout_height="0px"
        android:background="#ff0000"
        android:orientation="horizontal"
        android:gravity="center"
        android:weightSum="1.0"
        app:proportion="1.66666666666666"
        >
        <TextView
            android:layout_weight="0.6"
            android:layout_width="0px"
            android:layout_height="fill_parent"
            android:text="View 1"
            android:background="#0000ff"
            />
        <TextView
            android:layout_weight="0.4"
            android:layout_width="0px"
            android:layout_height="fill_parent"
            android:text="View 2.\nWidth takes 40% of the parent, and width = 2/3 height"
            android:background="#00ff00"
            />
    </com.chauhai.android.batsg.widget.ProportionalLayout>
    <TextView
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="Same as above, but in this case, the height of the parent (ProportionalLayout) is specified, and the width is adjusted based on it. "
        />
    <com.chauhai.android.batsg.widget.ProportionalLayout
        android:layout_width="0px"
        android:layout_height="100dp"
        android:background="#8000ff00"
        android:orientation="horizontal"
        android:gravity="center"
        app:proportion="1.66666666666666"
        >
        <TextView
            android:layout_weight="0.6"
            android:layout_width="0px"
            android:layout_height="fill_parent"
            android:text="View 1"
            android:background="#0000ff"
            />
        <TextView
            android:layout_weight="0.4"
            android:layout_width="0px"
            android:layout_height="fill_parent"
            android:text="View 2.\nWidth takes 40% of the parent, and width = 2/3 height"
            android:background="#00ff00"
            />
    </com.chauhai.android.batsg.widget.ProportionalLayout>
</LinearLayout>
```

Please see the sample code in [android-batsg-demo](http://code.google.com/p/android-batsg/source/browse/android-batsg-demo/src/com/chauhai/android/batsg/demo/widget/ProportionalLayoutDemoActivity.java)