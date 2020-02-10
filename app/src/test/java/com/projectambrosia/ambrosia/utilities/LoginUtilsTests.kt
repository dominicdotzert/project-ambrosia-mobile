package com.projectambrosia.ambrosia.utilities

import org.junit.Test

class LoginUtilsTests {

    @Test
    fun isValidEmail() {
        val email = "test@test.com"
        val result = isValidEmail(email)

        assert(result)
    }

    @Test
    fun isInvalidEmail() {
        val email = "123"
        val result = isValidEmail(email)

        assert(!result)
    }
}