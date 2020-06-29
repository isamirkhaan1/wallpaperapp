package com.samirk.wallpaperapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.samirk.wallpaperapp.utils.setWallpaper

/**
 * Download image from a URL
 *
 */
fun downloadImg(context: Context, url: String) {

    Glide.with(context)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                setWallpaper(context = context, bitmap = resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }

        })
}