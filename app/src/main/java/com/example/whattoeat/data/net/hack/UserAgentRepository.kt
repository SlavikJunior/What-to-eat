package com.example.whattoeat.data.net.hack

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
            getRandomNotUsedUserAgent()

        val randomUa = list.random()

        Log.d(TAG, "Picked user-agent: $randomUa")

        usedUa.put(System.nanoTime(), randomUa)

        Log.d(TAG, "Returned user-agent: $randomUa")

        return randomUa
    }


    private fun getRandomNotUsedUserAgent(): String {
        val randomUa = list.random()

        Log.d(TAG, "Picked user-agent: $randomUa")

        if (usedUa.values.contains(randomUa)) {
            Log.d(TAG, "User-agent was using")

            sizeHandling()
            getRandomNotUsedUserAgent()
        }

        Log.d(TAG, "Returned user-agent: $randomUa")
        return randomUa
    }

    private fun sizeHandling() {
        val size = (usedUa.size * 1.5).toInt()

        Log.d(TAG, "Repeating $size times to remove used user-agents")

        if (size >= list.size)
            repeat(size) {
                usedUa.remove(usedUa.keys.sorted().random())
            }
    }

    companion object {
        private const val TAG = "TEST-TAG"
    }
}