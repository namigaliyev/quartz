package com.scheduler.quartz.app.util

import com.scheduler.quartz.app.model.JobData

class JobUtil {
    companion object {
        fun jobKeyGen(product: String, operationId: String) : String {
            return "scheduled-$product-$operationId"
        }
    }
}