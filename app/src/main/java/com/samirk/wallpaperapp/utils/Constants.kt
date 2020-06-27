package com.samirk.wallpaperapp.utils

/**
 * Constants that are not class specific and could be use anywhere
 */
class Constants {

    /**
     * Only acceptable theme values
     */
    enum class Theme { WHITE, BLACK, COLOR }

    companion object {

        const val EMPTY_STRING = ""

        val DEFAULT_THEME = Theme.COLOR
        const val DEFAULT_USER_ID = -1L
        const val DEFAULT_TOKEN = EMPTY_STRING
    }
}