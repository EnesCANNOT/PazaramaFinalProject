package com.candroid.pazaramafinalproject.data.remote.responses

import com.google.gson.annotations.SerializedName

data class GenerationVii(
    val icons: İcons,
    @SerializedName("ultra-sun-ultra-moon")
    val ultra_sun_ultra_moon: UltraSunUltraMoon
)