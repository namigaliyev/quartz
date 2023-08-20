package com.scheduler.quartz.adapter.mysql

import com.scheduler.quartz.adapter.domain.QuartzJob
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface QuartzJobRepository : JpaRepository<QuartzJob, UUID> {

    fun findQuartzJobByJobKey(jobKey: String) : Optional<QuartzJob>

}