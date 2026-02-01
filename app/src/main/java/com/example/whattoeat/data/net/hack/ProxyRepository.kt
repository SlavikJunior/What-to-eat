package com.example.whattoeat.data.net.hack

import android.annotation.SuppressLint
import android.content.Context
import com.example.whattoeat.R
import java.io.BufferedInputStream
import java.net.InetSocketAddress
import java.net.Proxy

class ProxyRepository(context: Context) {

    enum class SupportProxyType {
        HTTP,
        SOCKS4,
        SOCKS5
    }

    @SuppressLint("NewApi")
    private val http =
        BufferedInputStream(context.resources.openRawResource(R.raw.http)).readAllBytes()
    private val proxyStringListHTTP = String(http).split("\n")
    private val proxyListHTTP = proxyStringListHTTP.map { proxyString ->
        val hostAndPort = Pair(proxyString.split(":")[0], proxyString.split(":")[1])
        Proxy(Proxy.Type.HTTP, InetSocketAddress(hostAndPort.first, hostAndPort.second.toInt()))
    }

    @SuppressLint("NewApi")
    private val socks4 =
        BufferedInputStream(context.resources.openRawResource(R.raw.socks4)).readAllBytes()
    private val proxyStringListSocks4 = String(socks4).split("\n")
    private val proxyListSocks4 = proxyStringListSocks4.map { proxyString ->
        val hostAndPort = Pair(proxyString.split(":")[0], proxyString.split(":")[1])
        Proxy(Proxy.Type.SOCKS, InetSocketAddress(hostAndPort.first, hostAndPort.second.toInt()))
    }

    @SuppressLint("NewApi")
    private val socks5 =
        BufferedInputStream(context.resources.openRawResource(R.raw.socks5)).readAllBytes()
    private val proxyStringListSocks5 = String(socks5).split("\n")
    private val proxyListSocks5 = proxyStringListSocks5.map { proxyString ->
        val hostAndPort = Pair(proxyString.split(":")[0], proxyString.split(":")[1])
        Proxy(Proxy.Type.SOCKS, InetSocketAddress(hostAndPort.first, hostAndPort.second.toInt()))
    }

    private val usedHttpProxy = mutableMapOf<Long, Proxy>()
    private val usedSocks4Proxy = mutableMapOf<Long, Proxy>()
    private val usedSocks5Proxy = mutableMapOf<Long, Proxy>()

    fun getRandomProxy(
        proxyType: SupportProxyType = ProxyRepository.SupportProxyType.HTTP,
        notUsed: Boolean = true
    ): Proxy {
        if (notUsed)
            return getRandomNotUsedProxy(proxyType)

        val randomProxy = when (proxyType) {
            SupportProxyType.HTTP -> proxyListHTTP.random()
            SupportProxyType.SOCKS4 -> proxyListSocks4.random()
            SupportProxyType.SOCKS5 -> proxyListSocks5.random()
        }

        when (proxyType) {
            SupportProxyType.HTTP -> usedHttpProxy.put(System.nanoTime(), randomProxy)
            SupportProxyType.SOCKS4 -> usedSocks4Proxy.put(System.nanoTime(), randomProxy)
            SupportProxyType.SOCKS5 -> usedSocks5Proxy.put(System.nanoTime(), randomProxy)
        }

        return randomProxy
    }


    private fun getRandomNotUsedProxy(proxyType: SupportProxyType): Proxy {
        val randomProxy = when (proxyType) {
            SupportProxyType.HTTP -> proxyListHTTP.random()
            SupportProxyType.SOCKS4 -> proxyListSocks4.random()
            SupportProxyType.SOCKS5 -> proxyListSocks5.random()
        }

        return when (proxyType) {
            SupportProxyType.HTTP -> if (usedHttpProxy.values.contains(randomProxy)) {
                sizeHandling(proxyType)
                getRandomNotUsedProxy(proxyType)
            } else {
                usedHttpProxy.put(System.nanoTime(), randomProxy)
                randomProxy
            }

            SupportProxyType.SOCKS4 -> if (usedSocks4Proxy.values.contains(randomProxy)) {
                sizeHandling(proxyType)
                getRandomNotUsedProxy(proxyType)
            } else {
                usedSocks4Proxy.put(System.nanoTime(), randomProxy)
                randomProxy
            }

            SupportProxyType.SOCKS5 -> if (usedSocks5Proxy.values.contains(randomProxy)) {
                sizeHandling(proxyType)
                getRandomNotUsedProxy(proxyType)
            } else {
                usedSocks5Proxy.put(System.nanoTime(), randomProxy)
                randomProxy
            }
        }
    }

    private fun sizeHandling(proxyType: SupportProxyType) {
        when (proxyType) {
            SupportProxyType.HTTP -> {
                val size = (usedHttpProxy.size * 1.5).toInt()
                if (size >= proxyListHTTP.size)
                    repeat(size) {
                        usedHttpProxy.remove(usedHttpProxy.keys.sorted().random())
                    }
            }

            SupportProxyType.SOCKS4 -> {
                val size = (usedSocks4Proxy.size * 1.5).toInt()
                if (size >= proxyListSocks4.size)
                    repeat(size) {
                        usedSocks4Proxy.remove(usedSocks4Proxy.keys.sorted().random())
                    }
            }

            SupportProxyType.SOCKS5 -> {
                val size = (usedSocks5Proxy.size * 1.5).toInt()
                if (size >= proxyListSocks5.size)
                    repeat(size) {
                        usedSocks5Proxy.remove(usedSocks5Proxy.keys.sorted().random())
                    }
            }
        }
    }
}