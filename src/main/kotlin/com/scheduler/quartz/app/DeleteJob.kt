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
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Slf4j
@Service
@RequiredArgsConstructor
class DeleteJob(
    private val scheduler: SchedulerUtil,
    private val repository: QuartzJobRepository
): Handler<List<UUID>, Void?> {

    override fun handle(request: List<UUID>): Void? {

        val jobDetails = repository.findAllById(request.toList())

        log.info("[${jobDetails.size}] ready for deletion")

        for (job in jobDetails) {
            job.status = Status.UNSCHEDULED

            val jobKey = jobKeyGen(
                    product = job.product!!,
                    operationId = job.operationId!!
            )
            scheduler.delete(jobKey)
        }

        repository.saveAll(jobDetails)

        log.info("jobs with keys=[${jobDetails.stream().map(QuartzJob::jobKey).toList()} deleted")

        return null
    }

    companion object {
        private var log = LoggerFactory.getLogger(this::class.java)
    }
}