package com.candroid.pazaramafinalproject.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Generationİii(
    val emerald: Emerald,
    @SerializedName("firered-leafgreen")
    val firered_leafgreen: FireredLeafgreen,
    @SerializedName("ruby-sapphire")
    val ruby_sapphire: RubySapphire
)