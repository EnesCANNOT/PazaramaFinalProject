package com.candroid.pazaramafinalproject.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Animated(

    @SerializedName("is_hidden")
    val isHidden: Boolean,
    val back_default: String,
    val back_female: Any,
    val back_shiny: String,
    val back_shiny_female: Any,
    val front_default: String,
    val front_female: Any,
    val front_shiny: String,
    val front_shiny_female: Any
)