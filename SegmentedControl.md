# Introduction #

![http://android-batsg.googlecode.com/svn/trunk/docs/images/SegmentedControlViewDemo.png](http://android-batsg.googlecode.com/svn/trunk/docs/images/SegmentedControlViewDemo.png)

In Android, to implement a Segmented control like iPhone application, just use RadioGroup and RadioButton, and customize the RadioButtons' layout so that they appear like Segmented control buttons.

With the help of [SegmentedControlView](http://code.google.com/p/android-batsg/source/browse/android-batsg/src/com/chauhai/android/batsg/widget/SegmentedControlView.java), this work is a little easier.

# Details #

Below this an example of using SegmentedControlView.

## The XML layout ##
You specify the firstButtonBackground, lastButtonBackground, middleButtonBackground attributes of SegmentedControlView here.

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:segmented="http://schemas.android.com/apk/res/com.chauhai.android.batsg.demo"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="vertical" >
    <com.chauhai.android.batsg.widget.SegmentedControlView
        android:id="@+id/segmented_control_1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="5dip"
        android:checkedButton="@+id/button_first"
        segmented:firstButtonBackground="@drawable/segmented_button_background_first"
        segmented:lastButtonBackground="@drawable/segmented_button_background_last"
        segmented:middleButtonBackground="@drawable/segmented_button_background_middle"
        segmented:uniqueButtonBackground="@drawable/segmented_button_background_unique"
        >
        <RadioButton
            android:id="@+id/button_first"
            android:minWidth="70dp"
            android:text="First"
            android:button="@null"
            android:gravity="center"
            android:padding="5dp"
            android:textAppearance="?android:attr/textAppearanceSmall"
            android:textColor="@color/segmented_button_text" />
        <RadioButton
            android:minWidth="70dp"
            android:text="Middle"
            android:button="@null"
            android:gravity="center"
            android:padding="5dp"
            android:textAppearance="?android:attr/textAppearanceSmall"
            android:textColor="@color/segmented_button_text" />
        <RadioButton
            android:minWidth="70dp"
            android:text="Last"
            android:button="@null"
            android:gravity="center"
            android:padding="5dp"
            android:textAppearance="?android:attr/textAppearanceSmall"
            android:textColor="@color/segmented_button_text" />
    </com.chauhai.android.batsg.widget.SegmentedControlView>
    <com.chauhai.android.batsg.widget.SegmentedControlView
        android:id="@+id/segmented_control_2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="5dip"
        segmented:uniqueButtonBackground="@drawable/segmented_button_background_unique"
        >
        <RadioButton
            android:id="@+id/button_unique"
            android:minWidth="70dp"
            android:text="Only one button"
            android:button="@null"
            android:gravity="center"
            android:padding="5dp"
            android:textAppearance="?android:attr/textAppearanceSmall"
            android:textColor="@color/segmented_button_text" />
    </com.chauhai.android.batsg.widget.SegmentedControlView>
</LinearLayout>
```

## The background for the first button ##
```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_checked="true" >
        <shape android:shape="rectangle">
            <gradient
                android:startColor="#6d6d6d"
                android:endColor="#000000"
                android:angle="270" />
            <stroke
                android:width="1dp"
                android:color="#38393a" />
            <!-- Don't know why I should specify bottomRight for bottomLeft -->
            <!-- android:radious is needed for the not-rounded corners. -->
            <corners
                android:radius="1dp"
                android:topLeftRadius="5dp"
                android:bottomLeftRadius="0dp"
                android:topRightRadius="0dp"
                android:bottomRightRadius="5dp"
                />
        </shape>
    </item>
    <item android:state_pressed="true" >
        <shape android:shape="rectangle">
            <gradient
                android:startColor="#6d6d6d"
                android:endColor="#000000"
                android:angle="270" />
            <stroke
                android:width="1dp"
                android:color="#38393a" />
            <corners
                android:radius="1dp"
                android:topLeftRadius="5dp"
                android:bottomLeftRadius="0dp"
                android:topRightRadius="0dp"
                android:bottomRightRadius="5dp"
                />
        </shape>
    </item>
    <item>
        <shape android:shape="rectangle">
            <gradient
                android:startColor="#81838a"
                android:endColor="#222732"
                android:angle="270" />
            <stroke
                android:width="1dp"
                android:color="#38393a" />
            <corners
                android:radius="1dp"
                android:topLeftRadius="5dp"
                android:bottomLeftRadius="0dp"
                android:topRightRadius="0dp"
                android:bottomRightRadius="5dp"
                />
        </shape>
    </item>
</selector>
```

Please see the sample code in [android-batsg-demo](http://code.google.com/p/android-batsg/source/browse/android-batsg-demo/src/com/chauhai/android/batsg/demo/widget/SegmentedControlDemoActivity.java)