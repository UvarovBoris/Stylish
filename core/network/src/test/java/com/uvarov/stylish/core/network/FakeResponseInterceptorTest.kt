package com.uvarov.stylish.core.network

import android.content.Context
import android.content.res.AssetManager
import com.uvarov.stylish.core.network.fake.FakeResponseInterceptor
import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream

class FakeResponseInterceptorTest {

    private val context: Context = mockk()
    private val assetManager: AssetManager = mockk()
    private val chain: Interceptor.Chain = mockk()
    private lateinit var interceptor: FakeResponseInterceptor

    @Before
    fun setUp() {
        every { context.assets } returns assetManager
        interceptor = FakeResponseInterceptor(context)
    }

    @Test
    fun intercept_imageJpgUrl_returns200AndJpegContentType() {
        val dummyBytes = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
        every { assetManager.open("mock/images/kurta.jpg") } returns ByteArrayInputStream(dummyBytes)

        val request = Request.Builder()
            .url("https://api.stylish.app/images/kurta.jpg")
            .build()
        every { chain.request() } returns request

        val response = interceptor.intercept(chain)

        assertEquals(200, response.code)
        assertEquals("image/jpeg", response.header("content-type"))
        assertNotNull(response.body)
        assertEquals(dummyBytes.size.toLong(), response.body?.contentLength())
    }

    @Test
    fun intercept_imagePngUrl_returns200AndPngContentType() {
        val dummyBytes = byteArrayOf(0x89.toByte(), 0x50.toByte(), 0x4E.toByte(), 0x47.toByte())
        every { assetManager.open("mock/images/cat_beauty.png") } returns ByteArrayInputStream(dummyBytes)

        val request = Request.Builder()
            .url("https://api.stylish.app/images/cat_beauty.png")
            .build()
        every { chain.request() } returns request

        val response = interceptor.intercept(chain)

        assertEquals(200, response.code)
        assertEquals("image/png", response.header("content-type"))
        assertNotNull(response.body)
    }
}
