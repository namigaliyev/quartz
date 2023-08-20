package com.scheduler.quartz.adapter.handler


fun interface Handler<REQ, RES> {
    @Throws(Exception::class)
    fun handle(request: REQ): RES
}