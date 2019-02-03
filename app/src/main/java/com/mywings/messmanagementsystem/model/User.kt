package com.mywings.messmanagementsystem.model

data class User(
    var id: Int = 0,
    var username: String = "",
    var password: String = "",
    var number: String = "",
    var localaddress: String = "",
    var name: String = "",
    var usertype: String = ""
)