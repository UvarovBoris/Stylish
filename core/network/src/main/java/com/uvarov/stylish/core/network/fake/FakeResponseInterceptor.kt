package com.uvarov.stylish.core.network.fake

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeResponseInterceptor @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        // Simulate network latency (250ms)
        try {
            Thread.sleep(250)
        } catch (_: InterruptedException) {
            // ignore
        }

        val jsonString = when {
            path.endsWith("api/v1/home") || path.contains("api/v1/home") -> {
                loadAsset("mock/home_feed.json")
            }
            path.contains("api/v1/products") -> {
                loadAsset("mock/products.json")
            }
            else -> {
                return chain.proceed(request)
            }
        }

        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(jsonString.toResponseBody("application/json".toMediaType()))
            .addHeader("content-type", "application/json")
            .build()
    }

    private fun loadAsset(fileName: String): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            throw RuntimeException("Failed to read asset: $fileName", e)
        }
    }
}
