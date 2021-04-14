package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val overview: String,
    val genres: List<Genre>,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>
)
