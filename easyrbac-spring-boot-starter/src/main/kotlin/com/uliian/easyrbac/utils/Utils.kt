package com.uliian.easyrbac.utils

import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.getAuthHeader(): AuthHeader? {
    val header = this.getHeader("Authorization")
    if(header!=null){
        val splitStr = header.split(' ')
        return AuthHeader(splitStr[0],splitStr[1])
    }
    return null
}

data class AuthHeader(val schema: String, val token: String)