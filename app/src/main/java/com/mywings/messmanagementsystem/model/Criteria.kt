package com.mywings.messmanagementsystem.model

data class Criteria(
    var distance: Int = 0,
    var messType: String = "",
    var foodType: String = "",
    var rating: Double = 0.0,
    var periodType: String = ""
)
