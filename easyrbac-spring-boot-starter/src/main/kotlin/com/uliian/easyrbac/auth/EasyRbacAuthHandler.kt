package com.uliian.easyrbac.auth

import com.uliian.easyrbac.config.DefaultInstance
import com.uliian.easyrbac.config.DefaultSpringConstant
import com.uliian.easyrbac.config.EasyRbacConfig
import com.uliian.easyrbac.config.JwtConfig
import com.uliian.easyrbac.dto.StandardErrorResult
import com.uliian.easyrbac.dto.UserInfo
import com.uliian.easyrbac.exception.EasyRbacException
import com.uliian.easyrbac.service.IEasyRbacService
import com.uliian.easyrbac.service.ILocalTokenService
import com.uliian.easyrbac.utils.getAuthHeader
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class EasyRbacAuthHandler(private val easyRbacConfig: EasyRbacConfig,
                          private val jwtConfig: JwtConfig,
                          private val easyRbacService: IEasyRbacService,
                          private val localTokenService: ILocalTokenService) :
        HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (request.method == "OPTIONS") {
            return true
        }

        if (handler !is HandlerMethod) {
            return true
        }

        val handlerImpl = handler as HandlerMethod?
        val authInfo = handlerImpl!!.getMethodAnnotation(Auth::class.java)

        if (!this.match(request, authInfo)) {
            return true
        }
        try {
            val userInfo = this.verifyUser(request)
            request.setAttribute(DefaultSpringConstant.USER_INFO_ATTR, userInfo)
            if (easyRbacConfig.checkPermission) {
                this.checkPermission(userInfo, authInfo)
            }
            return true
        } catch (e: EasyRbacException) {
            response.status = e.httpCode
            response.setHeader("Access-Control-Allow-Origin", "*")//* or origin as u prefer
            response.setHeader("Access-Control-Allow-Credentials", "true")
            response.setHeader("Access-Control-Allow-Methods", "PUT, POST, GET, OPTIONS, DELETE")
            response.setHeader("Access-Control-Max-Age", "3600")
            response.setHeader("Access-Control-Allow-Headers", "content-type, authorization")
            response.contentType = "application/json"

            val json = DefaultInstance.defaultJackson.writeValueAsString(StandardErrorResult(e.message,e.httpCode))
            response.writer.write(json)
            return false
        }

    }

    private fun match(request: HttpServletRequest, authInfo: Auth?): Boolean {
        return request.getAuthHeader()?.schema == this.jwtConfig.schema.toString() || authInfo != null
    }

    @Throws(EasyRbacException::class)
    private fun checkPermission(userInfo: UserInfo, authInfo: Auth) {
        if (authInfo.noCheckPermission) {
            return
        }
        if (!this.easyRbacService.hasPermission(userInfo.easyRbacToken, authInfo.resourceCode)) {
            throw EasyRbacException("没有资源权限", 403)
        }
    }

    @Throws(EasyRbacException::class)
    private fun verifyUser(request: HttpServletRequest): UserInfo {
        if(request.getAuthHeader() == null){
            throw EasyRbacException("未登录",401)
        }
//        return this.easyRbacService.getUserInfo(request.getAuthHeader()!!.token)
        return this.localTokenService.getUserInfoByLocalToken(request.getAuthHeader()!!.token)
    }
}