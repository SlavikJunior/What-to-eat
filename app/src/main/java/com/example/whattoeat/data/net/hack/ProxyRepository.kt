package com.example.whattoeat.data.net.hack

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.whattoeat.R
import com.example.whattoeat.data.net.hack.json_models.ProxyManager
import java.net.Proxy

@SuppressLint("NewApi")
class ProxyRepository(context: Context) {

    private val proxyManager = ProxyManager()

    init {
        proxyManager.load(String(context.resources.openRawResource(R.raw.proxylist).readAllBytes()))
    }

    fun getRandomProxy(): Proxy {
        var proxyItem = proxyManager.random()
        while (!proxyManager.test(proxyItem)) {
            proxyItem = proxyManager.random()
            Log.d(TAG, "Try ProxyItem: $proxyItem")
        }

        val randomProxy: Proxy = proxyItem.toProxy()

        Log.d(TAG, "Picked proxy: $randomProxy")

        Log.d(TAG, "Returned proxy: $randomProxy")

        return randomProxy
    }

    companion object {
        private const val TAG = "TEST TAG"
    }
}