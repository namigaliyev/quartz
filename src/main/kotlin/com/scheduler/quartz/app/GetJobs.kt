package com.scheduler.quartz.app

import com.scheduler.quartz.adapter.domain.QuartzJob
import com.scheduler.quartz.adapter.handler.Handler
import com.scheduler.quartz.adapter.mysql.QuartzJobRepository
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service

@Slf4j
@Service
@RequiredArgsConstructor
class GetJobs(
    private val repository: QuartzJobRepository
): Handler<Void?, List<QuartzJob>> {

    override fun handle(request: Void?): List<QuartzJob> {
        return repository.findAll()
    }
}