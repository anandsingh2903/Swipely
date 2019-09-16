package com.anand.swipelylib

import android.content.Context
import android.content.res.Resources
import android.media.AudioManager
import android.os.Handler
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

open class Swipley : AppCompatActivity() {

    private var audio: AudioManager? = null
    private var customView: CustomView? = null
    private var maxVolume: Int = 0
    private var currentVolume: Int = 0
    private var brightness: Float = 0.toFloat()
    private var deltaX: Float = 0.toFloat()
    private var deltaY: Float = 0.toFloat()
    private var maxValX: Float = 0.toFloat()
    private var maxValY: Float = 0.toFloat()
    private var firstTouchX: Float = 0.toFloat()
    private var firstTouchY: Float = 0.toFloat()
    private var currentX: Float = 0.toFloat()
    private var currentY: Float = 0.toFloat()
    private var SWIPE_THRESHOLD = 10.0f

    fun set(context: Context) {
        customView = CustomView(context)
        //seekView = SeekView(context)
        brightness = android.provider.Settings.System.getFloat(
            contentResolver,
            android.provider.Settings.System.SCREEN_BRIGHTNESS,
            -1f
        )
        val layout = window.attributes
        layout.screenBrightness = brightness / 255
        //        layout.screenBrightness = 1;
        window.attributes = layout
        customView?.setProgress((brightness / 255 * 100).toInt())
        customView?.setProgress(200 / 255 * 100)
        customView?.setProgressText(Integer.valueOf((brightness / 255 * 100).toInt()).toString() + "%")
        //circularSeekBar = CircularSeekBar(context)
        audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        currentVolume = audio!!.getStreamVolume(AudioManager.STREAM_MUSIC)
        maxVolume = audio!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        //val action = MotionEventCompat.getActionMasked(ev)

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                //Register the first touch on TouchDown and this should not change unless finger goes up.
                firstTouchX = ev.x
                firstTouchY = ev.y
                maxValX = 0.0f
                maxValY = 0.0f
                //As the event is consumed, return true
                //result = true
            }

            MotionEvent.ACTION_MOVE -> {
                //CurrentX/Y are the continues changing values of one single touch session. Change
                //when finger slides on view
                currentX = ev.x
                currentY = ev.y
                //setting the maximum value of X or Y so far. Any deviation in this means a  change of direction so reset the firstTouch value to last known max value i.e MaxVal X/Y.
                if (maxValX < currentX) {
                    maxValX = currentX
                } else {
                    firstTouchX = maxValX
                    maxValX = 0.0f
                }

                if (maxValY < currentY) {
                    maxValY = currentY
                } else {
                    firstTouchY = maxValY
                    maxValY = 0.0f
                }
                //DeltaX/Y are the difference between current touch and the value when finger first touched screen.
                //If its negative that means current value is on left side of first touchdown value i.e Going left and
                //vice versa.
                deltaX = currentX - firstTouchX
                deltaY = currentY - firstTouchY

                if (abs(deltaX) > abs(deltaY)) {
                    //Horizontal swipe
                    if (abs(deltaX) > SWIPE_THRESHOLD) {
                        if (deltaX > 0 &&
                            abs(firstTouchX) > abs(Resources.getSystem().displayMetrics.widthPixels / 3) &&
                            abs(firstTouchX) < Resources.getSystem().displayMetrics.widthPixels - abs(
                                Resources.getSystem().displayMetrics.widthPixels / 3
                            )
                        ) {
                            //means we are going right
                            //onRightSwipe(currentX)
                        } else if (abs(firstTouchX) > abs(Resources.getSystem().displayMetrics.widthPixels / 3) &&
                            abs(firstTouchX) < Resources.getSystem().displayMetrics.widthPixels - abs(
                                Resources.getSystem().displayMetrics.widthPixels / 3
                            )
                        ) {
                            //means we are going left
                            //onLeftSwipe(currentX)
                        }
                    }
                } else {
                    //It's a vertical swipe
                    resources
                    //System.out.println("Screen Width : "+Resources.getSystem().getDisplayMetrics().widthPixels/2);
                    //System.out.println("First Touch X: "+firstTouchX+" First Touch Y: "+firstTouchY);

                    if (abs(firstTouchX) > Resources.getSystem().displayMetrics.widthPixels - abs(
                            Resources.getSystem().displayMetrics.widthPixels / 3
                        )
                    ) {
                        if (abs(deltaY) > SWIPE_THRESHOLD) {
                            if (deltaY > 0) {
                                //means we are going down
                                //onDownSwipe(currentY)
                                decreaseVolume()
                            } else {
                                //means we are going up
                                //onUpSwipe(currentY)
                                increaseVolume()
                            }
                        }
                    } else if (abs(firstTouchX) < abs(Resources.getSystem().displayMetrics.widthPixels / 3)) {
                        if (abs(deltaY) > SWIPE_THRESHOLD) {
                            if (deltaY > 0) {
                                //means we are going down
                                //onBrightDownSwipe(currentY)
                                decreaseBrightness()
                                //commonBrightness(currentY)
                            } else {
                                //means we are going up
                                //onBrightUpSwipe(currentY)
                                increaseBrightness()
                                //commonBrightness(currentY)
                            }
                        }
                    }
                }

                //result = true
            }

            MotionEvent.ACTION_UP -> {
                //Clean UP
                firstTouchX = 0.0f
                firstTouchY = 0.0f

                val handler = Handler()
                handler.postDelayed({
                    if (customView!!.isVisible())
                        customView!!.hide()
                }, 2000)
                //result = true
            }

            else -> return false
        }

        return false

    }

    private fun increaseBrightness() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        customView?.setTitle("Brightness")
        customView?.show()

        val layout = window.attributes
        val currentScreenBrightness = layout.screenBrightness

        val updatedScreenBrightness = currentScreenBrightness + 0.02f

        val brightnessPercent : Int

        if ((updatedScreenBrightness) <= 1.0) {
            layout.screenBrightness = updatedScreenBrightness

            brightnessPercent = (currentScreenBrightness * 100).toInt()
        }
        else {
            brightnessPercent = 100
        }

        window.attributes = layout

        customView?.setProgress(brightnessPercent)
        customView?.setProgressText("$brightnessPercent %")
    }

    private fun decreaseBrightness() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val layout = window.attributes
        val currentScreenBrightness = layout.screenBrightness

        val updatedScreenBrightness = currentScreenBrightness - 0.02f

        val brightnessPercent : Int

        if ((updatedScreenBrightness) >= 0.0) {
            layout.screenBrightness = updatedScreenBrightness

            brightnessPercent = (currentScreenBrightness * 100).toInt()
        }
        else {
            brightnessPercent = 0
        }

        window.attributes = layout

        customView?.setTitle("Brightness")
        customView?.show()
        customView?.setProgress(brightnessPercent)
        customView?.setProgressText("$brightnessPercent %")
    }

    private fun decreaseVolume() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val currentVolume = audio!!.getStreamVolume(AudioManager.STREAM_MUSIC)
        audio!!.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume - 1, 0)

        val volumePercent: Int

        volumePercent = if (currentVolume > 0) {
            ((currentVolume - 1) * 100) / maxVolume
        } else {
            0
        }

        customView?.setTitle("  Volume  ")
        customView?.show()
        customView?.setProgress(volumePercent)
        customView?.setProgressText("$volumePercent %")
    }

    private fun increaseVolume() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val currentVolume = audio!!.getStreamVolume(AudioManager.STREAM_MUSIC)
        audio!!.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume + 1, 0)

        val volumePercent: Int

        volumePercent = if (currentVolume < maxVolume) {
            ((currentVolume + 1) * 100) / maxVolume
        } else {
            100
        }

        customView?.setTitle("  Volume  ")
        customView?.show()
        customView?.setProgress(volumePercent)
        customView?.setProgressText("$volumePercent %")
    }

}