package me.zaksen.pms.data

import kotlinx.serialization.Serializable

@Serializable
data class IgnoreList(
    var entries: MutableMap<String, MutableSet<String>> = mutableMapOf()
)
