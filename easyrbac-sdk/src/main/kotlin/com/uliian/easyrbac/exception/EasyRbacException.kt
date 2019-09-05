package com.uliian.easyrbac.exception

class EasyRbacException(message: String,
                        /**
                         * Gets the value of httpCode.
                         *
                         * @return the value of httpCode
                         */
                        val httpCode: Int) : RuntimeException(message)