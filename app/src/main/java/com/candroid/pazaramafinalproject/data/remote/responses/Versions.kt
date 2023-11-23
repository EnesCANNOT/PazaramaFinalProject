package com.candroid.pazaramafinalproject.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Versions(
    @SerializedName("generation-i")
    val generation_i: Generationİ,
    @SerializedName("generation-ii")
    val generation_ii: Generationİi,
    @SerializedName("generation-iii")
    val generation_iii: Generationİii,
    @SerializedName("generation-iv")
    val generation_iv: Generationİv,
    @SerializedName("generation-v")
    val generation_v: GenerationV,
    @SerializedName("generation-vi")
    val generation_vi: GenerationVi,
    @SerializedName("generation-vii")
    val generation_vii: GenerationVii,
    @SerializedName("generation-viii")
    val generation_viii: GenerationViii
)