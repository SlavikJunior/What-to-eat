package com.example.whattoeat.data.translateApi

import com.example.whattoeat.data.translateApi.models.DetectRequest
import com.example.whattoeat.data.translateApi.models.DetectResponse
import com.example.whattoeat.data.translateApi.models.TranslateRequest
import com.example.whattoeat.data.translateApi.models.TranslateResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TranslateApiService {

    @POST("translate/v2/translate")
    suspend fun translate(
        @Body translateRequest: TranslateRequest
    ): TranslateResponse

    @POST("translate/v2/detect")
    suspend fun detect(
        @Body detectRequest: DetectRequest
    ): DetectResponse
}