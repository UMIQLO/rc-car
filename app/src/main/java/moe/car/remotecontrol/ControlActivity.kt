package moe.car.remotecontrol

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import android.view.View
import kotlinx.android.synthetic.main.activity_control.*
import moe.car.remotecontrol.util.connectSocket
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.Socket
import java.net.URL


class ControlActivity : AppCompatActivity(), View.OnClickListener, SurfaceHolder.Callback2 {

    private lateinit var surfaceHolder: SurfaceHolder

    //http://192.168.8.1:8083/?action=snapshot
    //val imageURL: String = "http://via.placeholder.com/${width}x${height}"
    private val imageURL: String = "http://192.168.8.1:8083/?action=snapshot"
    //private val imageURL: String = "http://via.placeholder.com/350x123"
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var runFlag: Boolean = false

    override fun surfaceRedrawNeeded(holder: SurfaceHolder?) {
        //connectToCamera(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        //connectToCamera(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        runFlag = false
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        runFlag = true
        //connectToCamera(holder)
    }

    // Global onClick
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnUp -> {
                controlCar("MD_Qian")
            }
            R.id.btnDown -> {
                controlCar("MD_Hou")
            }
            R.id.btnLeft -> {
                controlCar("MD_Zuo")
            }
            R.id.btnRight -> {
                controlCar("MD_You")
            }
            R.id.btnStop -> {
                controlCar("MD_Ting")
            }
        }
    }

    // Global setOnClickListener
    private fun setOnClickListener() {
        btnUp.setOnClickListener(this)
        btnDown.setOnClickListener(this)
        btnLeft.setOnClickListener(this)
        btnRight.setOnClickListener(this)
        btnStop.setOnClickListener(this)
    }

    private fun screenSize() {
        val dm = resources.displayMetrics
        screenWidth = dm.widthPixels
        screenHeight = dm.heightPixels
    }

    private fun connectToCamera(holder: SurfaceHolder?) {
        val bitmapOption = BitmapFactory.Options()
        bitmapOption.inPreferredConfig = Bitmap.Config.ARGB_8888
        Thread(Runnable {
            while (runFlag) {
                val c: Canvas? = holder!!.lockCanvas()
                try {
                    synchronized(this) {
                        val url = URL(imageURL)
                        val conn = url.openConnection() as HttpURLConnection

                        var image = BitmapFactory.decodeStream(conn.inputStream, null, bitmapOption)
                        image = Bitmap.createBitmap(image)

                        c?.drawBitmap(image, 0f, 0f, null)
                        conn.disconnect()

                        Thread.sleep(40)
                    }
                } catch (e: Exception) {

                } finally {
                    holder.unlockCanvasAndPost(c)
                }
            }
        }).start()
    }

    private fun controlCar(order: String) {
        Thread(Runnable {
            connectSocket("192.168.9.6", 2001, order)
        }).start()
    }

    override fun onBackPressed() {
        runFlag = false
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        setOnClickListener()
        screenSize()
        surfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(this)
    }
}
