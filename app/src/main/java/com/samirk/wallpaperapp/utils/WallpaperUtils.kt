package com.samirk.wallpaperapp.utils

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import com.samirk.wallpaperapp.WallpaperService

/**
 * Set device wallpaper
 */
fun setWallpaper(context: Context, bitmap: Bitmap) {
    val wm = context.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
    wm.setBitmap(bitmap)

    //update lock screen as well
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        wm.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
    }

    //update last update
    PrefUtils.getInstance(context).wallpaperLastUpdated = getCurrentTimeMillis()

    //  stop downloading service
    WallpaperService.stop(context)
}