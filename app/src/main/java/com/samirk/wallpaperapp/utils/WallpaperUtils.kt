package com.samirk.wallpaperapp.utils

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap

/**
 * Set device wallpaper
 */
fun setWallpaper(context: Context, bitmap: Bitmap) {
    val wm = context.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
    wm.setBitmap(bitmap)

    //TODO set lock screen as well
}