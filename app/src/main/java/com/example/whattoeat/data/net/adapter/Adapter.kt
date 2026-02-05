package com.example.whattoeat.data.net.adapter

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

import okhttp3.Request
import okio.Timeout
import retrofit2.*


class ResultCallAdapterFactory(
    private val failureHandler: FailureHandler? = null
) : CallAdapter.Factory() {

    fun interface FailureHandler {
        fun onFailure(throwable: Throwable)
    }

    private var hasConverterForResult: Boolean? = null
    private fun Retrofit.hasConverterForResultType(resultType: Type): Boolean {
        return if (hasConverterForResult == true) true else runCatching {
            nextResponseBodyConverter<Result<*>>(
                null, resultType, arrayOf()
            )
        }.isSuccess.also { hasConverterForResult = it }
    }

    private class CallDataType(
        private val dataType: Type
    ) : ParameterizedType {
        override fun getActualTypeArguments(): Array<Type> = arrayOf(dataType)
        override fun getRawType(): Type = Call::class.java
        override fun getOwnerType(): Type? = null
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        if (returnType !is ParameterizedType) return null

        val resultType: Type = getParameterUpperBound(0, returnType)
        if (getRawType(resultType) != Result::class.java
            || resultType !is ParameterizedType
        ) return null

        val dataType = getParameterUpperBound(0, resultType)

        val delegateType = if (retrofit.hasConverterForResultType(resultType))
            returnType else CallDataType(dataType)

        val delegate: CallAdapter<*, *> = retrofit
            .nextCallAdapter(this, delegateType, annotations)

        return CatchingCallAdapter(delegate, failureHandler)
    }

    private class CatchingCallAdapter(
        private val delegate: CallAdapter<*, *>,
        private val failureHandler: FailureHandler?
    ) : CallAdapter<Any, Call<Result<*>>> {
        override fun responseType(): Type = delegate.responseType()
        override fun adapt(call: Call<Any>): Call<Result<*>> = CatchingCall(call, failureHandler)
    }

    private class CatchingCall(
        private val delegate: Call<Any>,
        private val failureHandler: FailureHandler?
    ) : Call<Result<*>> {

        override fun enqueue(callback: Callback<Result<*>>) = delegate.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    callback.onResponse(this@CatchingCall, Response.success(Result.success(body)))
                } else {
                    val throwable = HttpException(response)
                    failureHandler?.onFailure(throwable)
                    callback.onResponse(
                        this@CatchingCall,
                        Response.success(Result.failure<Any>(throwable))
                    )
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                failureHandler?.onFailure(t)
                callback.onResponse(
                    this@CatchingCall,
                    Response.success(Result.failure<Any>(t))
                )
            }
        })

        override fun clone(): Call<Result<*>> = CatchingCall(delegate, failureHandler)
        override fun execute(): Response<Result<*>> =
            throw UnsupportedOperationException("Suspend function should not be blocking.")

        override fun isExecuted(): Boolean = delegate.isExecuted
        override fun cancel(): Unit = delegate.cancel()
        override fun isCanceled(): Boolean = delegate.isCanceled
        override fun request(): Request = delegate.request()
        override fun timeout(): Timeout = delegate.timeout()
    }
}