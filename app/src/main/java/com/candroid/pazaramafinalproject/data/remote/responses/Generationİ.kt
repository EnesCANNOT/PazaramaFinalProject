package com.candroid.pazaramafinalproject.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Generationİ(
    @SerializedName("red-blue")
    val red_blue: RedBlue,
    val yellow: Yellow
)