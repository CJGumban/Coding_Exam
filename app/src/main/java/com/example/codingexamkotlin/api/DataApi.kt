package com.example.codingexamkotlin.api

import com.example.codingexamkotlin.model.UserInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DataApi{

    @POST("v3/e0d47bd1-6c7c-4153-b465-fcf73c3e0ea9")
    suspend fun submitDataOK(@Body userInfo: UserInfo): Response<Any?>

    @POST("v3/68dc845d-a015-4397-883c-97df8342a0fb")
    suspend fun submitDataNotFound(@Body userInfo: UserInfo): Response<Any?>

}

