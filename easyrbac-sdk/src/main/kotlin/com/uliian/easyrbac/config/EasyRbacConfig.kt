package com.uliian.easyrbac.config

class EasyRbacConfig {
    var appId: String = ""
    var appSecret: String = ""
    var url: String = ""
    var localTokenConfig: JwtConfig = JwtConfig()
    var checkPermission: Boolean = false
}

class JwtConfig {
    var key: String? = null
    var tokenLocation:String = ""
    var schema:SchemaType = SchemaType.AuthHeader
}

enum class SchemaType{
    AuthHeader,
    QueryString,
    Cookie
}