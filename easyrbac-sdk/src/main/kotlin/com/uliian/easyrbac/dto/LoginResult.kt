package com.uliian.easyrbac.dto

data class LoginResult(val schema: String,val token: String,val appCode: String,val expireIn:Int)