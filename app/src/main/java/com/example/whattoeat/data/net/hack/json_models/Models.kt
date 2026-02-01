package com.example.whattoeat.data.net.hack.json_models

import kotlinx.serialization.Serializable

@Serializable
data class ProxyResponse(
    val customProxyList: List<CustomProxyItem>
)

@Serializable
data class CustomProxyItem(
    val enabled: Boolean,
    val proxy: ProxyInfo
)

@Serializable
data class ProxyInfo(
    val address: String,
    val port: Int,
    val type: String,
    val username: String? = null,
    val password: String? = null,
    val responseTime: Long = Long.MAX_VALUE
)