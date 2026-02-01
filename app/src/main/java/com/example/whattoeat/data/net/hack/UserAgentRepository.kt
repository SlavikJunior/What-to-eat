package com.example.whattoeat.data.net.hack

import android.annotation.SuppressLint
import android.content.Context
import com.example.whattoeat.R
import java.io.BufferedInputStream

class UserAgentRepository(context: Context) {

    @SuppressLint("NewApi")
    private val ua =
        BufferedInputStream(context.resources.openRawResource(R.raw.user_agents)).readAllBytes()
    private val list = String(ua).split("\n")

    private val usedUa = mutableMapOf<Long, String>()

    fun getRandomUserAgent(notUsed: Boolean = true): String {
        if (notUsed)
            return getRandomNotUsedUserAgent()

        val randomUa = list.random()

        usedUa.put(System.nanoTime(), randomUa)

        return randomUa
    }


    private fun getRandomNotUsedUserAgent(): String {
        val randomUa = list.random()

        if (usedUa.values.contains(randomUa)) {
            sizeHandling()
            return getRandomNotUsedUserAgent()
        }

        return randomUa
    }

    private fun sizeHandling() {
        val size = (usedUa.size * 1.5).toInt()
        if (size >= ua.size)
            repeat(size) {
                usedUa.remove(usedUa.keys.sorted().random())
            }
    }
}