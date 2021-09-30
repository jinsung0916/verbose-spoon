package com.benope.verbose.spoon.core_backend.async.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

class CustomAsyncExceptionHandler : AsyncUncaughtExceptionHandler {

    val logger: Logger = LoggerFactory.getLogger(CustomAsyncExceptionHandler::class.java)

    override fun handleUncaughtException(
        throwable: Throwable, method: Method, vararg obj: Any
    ) {
        logger.error("Async exception: ", throwable)
    }

}
