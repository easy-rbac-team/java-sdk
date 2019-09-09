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
    var tokenLocation:SchemaType? = null
    var schema:String = ""
}

enum class SchemaType{
    AuthHeader,
    QueryString,
    Cookie
}