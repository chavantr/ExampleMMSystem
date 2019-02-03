package com.mywings.messmanagementsystem.process

import com.mywings.messmanagementsystem.model.User

interface OnLoginListener {
    fun onLoginSuccess(user: User)
}