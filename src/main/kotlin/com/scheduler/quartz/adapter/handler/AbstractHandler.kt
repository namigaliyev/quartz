package com.scheduler.quartz.adapter.handler

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*


@Component
@RequiredArgsConstructor
class AbstractHandler {
    @Autowired
    private final val handlerMap: Map<String, Handler<*, *>>? = null

    operator fun <T> get(aClass: Class<T>): T {
        val simpleName = aClass.getSimpleName()
        val beanName = simpleName.substring(0, 1).lowercase(Locale.getDefault()) + simpleName.substring(1)
        return handlerMap!![beanName] as T
    }
}
