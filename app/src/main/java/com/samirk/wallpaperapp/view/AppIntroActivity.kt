package com.samirk.wallpaperapp.view

import android.os.Bundle
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide
import com.samirk.wallpaperapp.R
import com.samirk.wallpaperapp.utils.Analytics

class AppIntroActivity : IntroActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Analytics.getInstance().logEvent(Analytics.EVENT_APP_INTRO)

        val slide1 = SimpleSlide.Builder()
            .title(getString(R.string.tit_how_to_use))
            .description(getString(R.string.msg_how_to_use))
            .image(R.drawable.ic_welcome)
            .background(R.color.color_red)
            .build()

        val slide2 = SimpleSlide.Builder()
            .title(getString(R.string.tit_intro_choose_theme))
            .description(getString(R.string.msg_intro_choose_theme))
            .image(R.drawable.ic_choose)
            .background(R.color.color_purple)
            .build()


        val slide3 = SimpleSlide.Builder()
            .title(getString(R.string.tit_daily_wallpaper))
            .description(getString(R.string.msg_daily_wallpaper))
            .image(R.drawable.ic_daily)
            .background(R.color.color_blue)
            .build()


        val slide4 = SimpleSlide.Builder()
            .title(getString(R.string.tit_unlimited_wallpapers))
            .description(getString(R.string.msg_unlimited_wallpapers))
            .image(R.drawable.ic_unlimited)
            .background(R.color.color_green)
            .build()

        val slide5 = SimpleSlide.Builder()
            .title(getString(R.string.tit_customization))
            .description(getString(R.string.msg_customization))
            .image(R.drawable.ic_sticker)
            .background(R.color.color_purple)
            .build()


        isButtonBackVisible = false
        isButtonNextVisible = false

        addSlide(slide1)
        addSlide(slide2)
        addSlide(slide3)
        addSlide(slide4)
        addSlide(slide5)
    }
}