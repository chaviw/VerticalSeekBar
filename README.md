# VerticalSeekBar

This is a custom vertical seek bar that works with Marshmallow operating system <br/>
See http://stackoverflow.com/questions/33112277/android-6-0-marshmallow-stops-showing-vertical-seekbar-thumb

Usage: 
```
<com.verticalseekbar.VerticalSeekBar
  android:layout_width="25dp"
  android:layout_height="match_parent"
  android:splitTrack="false"
  android:thumb="@android:color/transparent"
  app:customThumb="@drawable/thumb" />
```

You need to ensure that the default `android:thumb` is transparent so there aren't duplicate thumbs. Use the `app:customThumb` attribute to define the seek bar's thumb
