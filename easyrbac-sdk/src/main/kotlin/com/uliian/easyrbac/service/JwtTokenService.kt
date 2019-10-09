package com.uliian.easyrbac.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.uliian.easyrbac.config.JwtConfig
import com.uliian.easyrbac.dto.UserInfo
import com.uliian.easyrbac.exception.EasyRbacException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class JwtTokenService(private val jwtConfig: JwtConfig) : ILocalTokenService {
    private val algorithm: Algorithm = Algorithm.HMAC256(jwtConfig.key)
    private val require: JWTVerifier = JWT.require(algorithm).build()

    override fun getUserInfoByLocalToken(token: String): UserInfo {
        val tokenInfo: DecodedJWT
        try {
            tokenInfo = require.verify(token)
        } catch (e: JWTVerificationException) {
            throw EasyRbacException(e.message!!, 401)
        }
        val result = UserInfo()
        result.id = tokenInfo.claims.getValue("id").asString().toLong()
        result.userName = tokenInfo.claims.getValue("userName").asString()
        result.realName = tokenInfo.claims.getValue("realName").asString()
        result.mobilePhone = tokenInfo.claims.getValue("mobilePhone").asString()
        result.isEnable = tokenInfo.claims.getValue("isEnable").asBoolean()
        result.easyRbacToken = tokenInfo.claims.getValue("easyRbacToken").asString()
        return result
    }

    override fun generateLocalToken(userInfo: UserInfo): String {
        val expireAt = LocalDateTime.now().plusSeconds(this.jwtConfig.expireTimeSeconds.toLong())
        val expireDate = Date.from( expireAt.atZone( ZoneId.systemDefault()).toInstant())
        val jwt = JWT.create()
                .withClaim("id", userInfo.id.toString())
                .withClaim("userName", userInfo.userName)
                .withClaim("realName", userInfo.realName)
                .withClaim("mobilePhone", userInfo.mobilePhone)
                .withClaim("isEnable", userInfo.isEnable)
                .withClaim("easyRbacToken", userInfo.easyRbacToken)
                .withExpiresAt(expireDate)
                .sign(algorithm)
        return "${this.jwtConfig.schema} $jwt"
    }
}