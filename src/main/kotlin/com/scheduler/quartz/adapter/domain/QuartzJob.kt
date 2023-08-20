package com.scheduler.quartz.adapter.domain

import com.scheduler.quartz.app.model.Status
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.UUID

@Entity
@Table(name = "quartz_job")
class QuartzJob (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,
    // Unique job key for identity and triggering
    var jobKey: String? = null,

    var product: String? = null,

    var operationId: String? = null,

    var jobClassName: String? = null,

    var cronExpression: String? = null,

    var kafkaTopic: String? = null,

    var description: String? = "",

    @Enumerated(EnumType.STRING)
    var status: Status = Status.NORMAL,

    @CreationTimestamp
    val createdAt: String? = null,

    @UpdateTimestamp
    val updatedAt: String? = null
)