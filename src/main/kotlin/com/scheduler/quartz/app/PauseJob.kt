package com.scheduler.quartz.app

import com.scheduler.quartz.adapter.domain.QuartzJob
import com.scheduler.quartz.adapter.handler.Handler
import com.scheduler.quartz.adapter.mysql.QuartzJobRepository
import com.scheduler.quartz.app.model.JobData
import com.scheduler.quartz.app.model.Status
import com.scheduler.quartz.app.util.JobUtil.Companion.jobKeyGen
import com.scheduler.quartz.app.util.SchedulerUtil
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.quartz.Scheduler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Slf4j
@Service
@RequiredArgsConstructor
class PauseJob(
    private val scheduler: SchedulerUtil,
    private val repository: QuartzJobRepository
): Handler<JobData<*>, Void?> {

    override fun handle(request: JobData<*>): Void? {
        val jobKey = jobKeyGen(
            product = request.product,
            operationId = request.operationId
        )

        val job = repository.findQuartzJobByJobKey(jobKey)
            .orElseThrow { RuntimeException() }

        scheduler.pause(jobKey)

        job.status = Status.PAUSED

        repository.save(job)

        return null
    }

    companion object {
        private var log = LoggerFactory.getLogger(this::class.java)
    }
}