package com.scheduler.quartz.adapter.controller

import com.scheduler.quartz.adapter.domain.QuartzJob
import com.scheduler.quartz.adapter.handler.AbstractHandler
import com.scheduler.quartz.adapter.mysql.QuartzJobRepository
import com.scheduler.quartz.app.CreateJob
import com.scheduler.quartz.app.DeleteJob
import com.scheduler.quartz.app.GetJobs
import com.scheduler.quartz.app.PauseJob
import com.scheduler.quartz.app.model.JobData
import com.scheduler.quartz.app.util.JobUtil
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*


@Slf4j
@RestController
@RequestMapping("/v1/job")
@RequiredArgsConstructor
class JobController (
    private val handler: AbstractHandler,
    private val repository: QuartzJobRepository
) {

    @GetMapping
    fun getJobs(): List<QuartzJob> {
        return handler[GetJobs::class.java]
            .handle(null)
    }

    @PostMapping
    fun createJob(@RequestBody job: JobData<*>) : Void? {
        handler[CreateJob::class.java]
            .handle(job)
        return null;
    }

    @PostMapping("/pause")
    fun pauseJob(@RequestBody job: JobData<*>) : Void? {
        handler[PauseJob::class.java]
            .handle(job)
        return null;
    }

    @PutMapping
    fun updateJob(@RequestBody job: JobData<*>) : Void? {
        val jobKey = JobUtil.jobKeyGen(
            product = job.product,
            operationId = job.operationId
        )
        val jobDetail = repository.findQuartzJobByJobKey(jobKey)
            .orElseThrow{ RuntimeException() }

        handler[DeleteJob::class.java]
            .handle(listOf(jobDetail.id!!))

        handler[CreateJob::class.java]
            .handle(job)
        return null;
    }

    @DeleteMapping
    fun deleteJob(@RequestParam ids : List<UUID>) : Void? {
        handler[DeleteJob::class.java]
            .handle(ids)
        return null
    }
}