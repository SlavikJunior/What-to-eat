package com.example.whattoeat.data.net.hack.json_models

import kotlinx.serialization.json.Json
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.Proxy
import java.util.concurrent.TimeUnit

class ProxyManager {

    private val json = Json { ignoreUnknownKeys = true }
    private val proxies = mutableListOf<ProxyItem>()

    fun load(jsonString: String) {
        val response = json.decodeFromString<ProxyResponse>(jsonString)

        proxies += response.customProxyList
            .asSequence()
            .filter { it.enabled }
            .map {
                val p = it.proxy
                ProxyItem(
                    host = p.address,
                    port = p.port,
                    type = when (p.type.uppercase()) {
                        "SOCKS", "SOCKS5" -> Proxy.Type.SOCKS
                        else -> Proxy.Type.HTTP
                    },
                    user = p.username,
                    pass = p.password,
                    responseTime = p.responseTime
                )
            }
            .toList()
    }

    fun random(): ProxyItem = proxies.random()

    fun fastest(): ProxyItem = proxies.minBy { it.responseTime }

    fun byType(type: Proxy.Type): List<ProxyItem> = proxies.filter { it.type == type }

    fun size(): Int = proxies.size

    fun test(item: ProxyItem): Boolean = try {
        val client = OkHttpClient.Builder()
            .proxy(item.toProxy())
            .apply {
                if (item.user != null && item.pass != null) {
                    proxyAuthenticator { _, r ->
                        r.request.newBuilder()
                            .header(
                                "Proxy-Authorization",
                                Credentials.basic(item.user, item.pass)
                            )
                            .build()
                    }
                }
            }
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()

        client.newCall(
            Request.Builder()
                .url("https://www.russianfood.com/")
                .build()
        ).execute().use { it.isSuccessful }

    } catch (_: Exception) {
        false
    }
}
