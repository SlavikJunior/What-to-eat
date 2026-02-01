package com.example.whattoeat.data.net.hack.json_models

import java.net.InetSocketAddress
import java.net.Proxy

data class ProxyItem(
    val host: String,
    val port: Int,
    val type: Proxy.Type,
    val user: String? = null,
    val pass: String? = null,
    val responseTime: Long
) {
    fun toProxy(): Proxy =
        Proxy(type, InetSocketAddress(host, port))
}