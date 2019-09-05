package com.ynhdkc.link.config

import com.uliian.easyrbac.config.DefaultSpringConstant
import com.uliian.easyrbac.dto.UserInfo
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class UserInfoArgumentResolver : HandlerMethodArgumentResolver {
    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        return webRequest.getAttribute(DefaultSpringConstant.USER_INFO_ATTR,0)
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == UserInfo::class.java
    }
}