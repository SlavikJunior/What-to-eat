package com.example.whattoeat.data.yandex_translate

import com.example.whattoeat.data.yandex_translate.models.DetectRequest
import com.example.whattoeat.data.yandex_translate.models.DetectResponse
import com.example.whattoeat.data.yandex_translate.models.TranslateRequest
import com.example.whattoeat.data.yandex_translate.models.TranslateResponse
import retrofit2.http.Body
import retrofit2.http.Header
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