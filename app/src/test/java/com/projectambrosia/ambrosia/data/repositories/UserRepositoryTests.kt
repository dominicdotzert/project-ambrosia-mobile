package com.projectambrosia.ambrosia.data.repositories

import com.projectambrosia.ambrosia.data.dao.UserDao
import com.projectambrosia.ambrosia.network.RequestManager
import com.projectambrosia.ambrosia.network.models.ResponseLogin
import com.projectambrosia.ambrosia.network.models.ResponseLogout
import com.projectambrosia.ambrosia.network.models.Tokens
import com.projectambrosia.ambrosia.utilities.PreferencesHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class UserRepositoryTests {

    private val userId = "123"

    @MockK private lateinit var requestManager: RequestManager
    @MockK private lateinit var prefs: PreferencesHelper
    @MockK private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository

    @Before
    fun init() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        userRepository = UserRepository(requestManager, prefs, userDao)
    }

    @Test
    fun testLogUserIn() {
        val email = "test@test.com"
        val password = "password1"
        val expectedResponse = ResponseLogin("Success",Tokens("access_token", "refresh_token"))
        coEvery { requestManager.loginRequest(email, password) } returns expectedResponse

        val response = runBlocking { userRepository.logUserIn(email, password) } as ResponseLogin

        assertThat(response, equalTo(expectedResponse))
        coVerify { requestManager.loginRequest(email, password) }
    }

    @Test
    fun testLogUserOut() {
        val expectedResponse = ResponseLogout("Success")
        coEvery { requestManager.logoutRequest() } returns expectedResponse

        val response = runBlocking { userRepository.logUserOut() } as ResponseLogout

        assertThat(response, equalTo(expectedResponse))
        coVerify { requestManager.logoutRequest() }
    }
}