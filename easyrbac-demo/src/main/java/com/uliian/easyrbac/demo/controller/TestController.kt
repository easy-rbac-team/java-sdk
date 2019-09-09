package com.uliian.easyrbac.demo.controller

import com.uliian.easyrbac.auth.Auth
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping("/1")
    fun test1(){

    }

//    @Auth(noCheckPermission = false,resourceCode = "test")
    @GetMapping("/2")
    fun test2(){

    }
}