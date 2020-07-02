package com.samirk.wallpaperapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.samirk.wallpaperapp.utils.setWallpaper
import timber.log.Timber

/**
 * Download image from a URL
 *
 */
fun downloadImg(context: Context, url: String) {

    Glide.with(context)
        .asBitmap()
        .load(url)
        .addListener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                Timber.e(e)

                stopService(context, Intent(context, WallpaperService::class.java))
                return true
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return true
            }

        })
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                setWallpaper(context = context, bitmap = resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }

        })

}