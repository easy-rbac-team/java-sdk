package com.uliian.easyrbac.config

class EasyRbacConfig {
    var appId: String = ""
    var appSecret: String = ""
    var url: String = "http://easyrbac.uliian.com"
//    var localTokenConfig: JwtConfig = JwtConfig()
    var checkPermission: Boolean = false
}

class JwtConfig {
    var key: String? = null
    var tokenLocation:SchemaType = SchemaType.AuthHeader
    var schema:String = ""
    var expireTimeSeconds:Int = 8*60*60*1000
}

enum class SchemaType{
    AuthHeader,
    QueryString,
    Cookie
}