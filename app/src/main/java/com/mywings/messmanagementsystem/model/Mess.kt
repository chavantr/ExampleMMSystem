package com.mywings.messmanagementsystem.model

data class Mess(
    var id: Int = 0,
    var name: String = "",
    var localArea: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var messType: String = "",
    var foodType: String = "",
    var rating: String = "",
    var periodType: String = "",
    var comment: String = ""
)