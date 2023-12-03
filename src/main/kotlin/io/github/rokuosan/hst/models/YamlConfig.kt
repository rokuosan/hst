package io.github.rokuosan.hst.models

import kotlinx.serialization.Serializable

@Serializable
data class YamlConfig(
    val path: String
)