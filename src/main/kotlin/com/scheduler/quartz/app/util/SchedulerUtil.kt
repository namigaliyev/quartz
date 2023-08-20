package com.scheduler.quartz.app.util

import com.scheduler.quartz.app.job.EventProducerJob
import com.scheduler.quartz.app.model.JobData
import com.scheduler.quartz.app.util.JobUtil.Companion.jobKeyGen
import org.quartz.*
import org.springframework.stereotype.Component
import java.lang.RuntimeException


@Component
class SchedulerUtil(
    private val scheduler: Scheduler
) {

    fun schedule(jobKey: String, jobData: JobData<*>) {
        try {
            val jobDetail = JobBuilder.newJob(EventProducerJob::class.java)
                .withIdentity(JobKey.jobKey(jobKey))
                .setJobData(jobDataMap(jobData))
                .build()

            val scheduleBuilder = CronScheduleBuilder.cronSchedule(jobData.cronExpression)
                .withMisfireHandlingInstructionDoNothing()

            val trigger = TriggerBuilder.newTrigger()
                .withIdentity(TriggerKey.triggerKey(jobKey))
                .withSchedule(scheduleBuilder)
                .build()

            scheduler.scheduleJob(jobDetail, trigger)
        } catch (ex : SchedulerException) {
            throw RuntimeException(ex)
        }

    }

    fun delete(jobKey: String) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobKey))
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey))
            scheduler.deleteJob(JobKey.jobKey(jobKey))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun pause(jobKey: String) {
        try {
            scheduler.pauseJob(JobKey.jobKey(jobKey))
        } catch (ex : SchedulerException) {
            throw RuntimeException(ex)
        }
    }

    private fun jobDataMap(jobData : JobData<*>) : JobDataMap {
        return JobDataMap(
            mapOf(
                "data" to jobData.data
            )
        )
    }
}