package com.moonsu.assignment.core.network

interface RemoteMapper<DataModel> {
    fun toData(): DataModel
}
