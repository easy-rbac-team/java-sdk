package com.uliian.easyrbac.dto

data class LoginResult(val schema: String,val token: String,val appCode: String,val expireIn:Int)

data class LoginRequest(val  userName:String,val password:String,val appCode:String)