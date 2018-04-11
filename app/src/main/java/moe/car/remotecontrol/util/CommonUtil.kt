package moe.car.remotecontrol.util

import android.app.Activity
import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun View.snackBar(view: View, message: CharSequence) =
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()

fun setImgToImageView(url: String, activity: Activity, imageView: ImageView) =
        Glide.with(activity).load(url).into(imageView)

fun connectSocket(host: String, port: Int, command: String) {
    var reader: BufferedReader? = null
    var writer: BufferedWriter? = null
    var socket: Socket? = null
    try {
        //1.建立客户端socket连接，指定服务器位置及端口
        socket = Socket(host, port)
        //2.从网络->内存 in
        reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
        //2.从内存->网络 out
        writer = BufferedWriter(OutputStreamWriter(socket!!.getOutputStream()))

        val inputContent: String = command

        //3.向客户端发送消息
        writer!!.write(inputContent)
        writer!!.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        //4.关闭资源
        try {
            reader!!.close()
            writer!!.close()
            socket!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
