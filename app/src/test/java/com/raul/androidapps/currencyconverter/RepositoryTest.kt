package com.raul.androidapps.currencyconverter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.raul.androidapps.currencyconverter.domain.model.Rates
import com.raul.androidapps.currencyconverter.domain.model.SingleRate
import com.raul.androidapps.currencyconverter.network.NetworkApi
import com.raul.androidapps.currencyconverter.network.NetworkServiceFactory
import com.raul.androidapps.currencyconverter.network.Resource
import com.raul.androidapps.currencyconverter.repository.RepositoryImpl
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response


class RepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var networkServiceFactory: NetworkServiceFactory

    @Mock
    lateinit var api: NetworkApi

    @InjectMocks
    private lateinit var repository: RepositoryImpl
    private lateinit var rates: Rates

    @Before
    fun setUp() {
        val list: List<SingleRate> = mutableListOf(
            SingleRate("EUR", 1.toBigDecimal(), true),
            SingleRate("USD", 0.90.toBigDecimal(), false),
            SingleRate("GBP", 1.12.toBigDecimal(), false)
        )
        rates = Rates(list)
        MockitoAnnotations.initMocks(this)

        whenever(networkServiceFactory.getServiceInstance())
            .thenReturn(
                api
            )
    }

    @Test
    fun testResponseSuccess() {
        runBlocking {
            val currency = "EUR"
            whenever(api.getLatestRatesWithCoroutines(currency))
                .thenReturn(
                    Response.success(rates)
                )
            val response = repository.getRatesWithCoroutines(currency)
            assertEquals(response.status, Resource.Status.SUCCESS)
            assertEquals(response.data, rates)
        }
    }

    @Test
    fun testResponseError() {
        runBlocking {
            val currency = "EUR"
            whenever(api.getLatestRatesWithCoroutines(currency))
                .thenReturn(
                    Response.error(404, "".toResponseBody(null))
                )
            val response = repository.getRatesWithCoroutines(currency)
            assertEquals(response.status, Resource.Status.ERROR)
            assertEquals(response.data, null)
        }
    }

    @Suppress("DIVISION_BY_ZERO")
    @Test
    fun testResponseException() {
        runBlocking {
            val currency = "EUR"
            whenever(api.getLatestRatesWithCoroutines(currency))
                .then {
                    1 / 0
                }
            val response = repository.getRatesWithCoroutines(currency)
            assertEquals(response.status, Resource.Status.ERROR)
            assertEquals(response.data, null)
        }
    }
}
