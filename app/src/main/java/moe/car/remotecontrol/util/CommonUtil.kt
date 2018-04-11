package moe.car.remotecontrol.util

import android.app.Activity
import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun View.snackBar(view: View, message: CharSequence) =
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()

fun setImgToImageView(url: String, activity: Activity, imageView: ImageView) =
        Glide.with(activity).load(url).into(imageView)
