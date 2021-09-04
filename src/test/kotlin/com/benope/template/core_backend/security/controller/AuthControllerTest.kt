package com.benope.template.core_backend.security.controller

import com.benope.template.BenopeTest
import com.benope.template.core_backend.security.dto.RefreshRequest
import com.benope.template.core_backend.security.dto.RefreshResponse
import com.benope.template.core_backend.security.dto.SignInRequest
import com.benope.template.core_backend.security.dto.SignInResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
internal class AuthControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper
) : BenopeTest() {


    @Test
    fun authenticationTest() {
        // Given
        val signInRequest = SignInRequest(USERNAME, PASSWORD)

        // When
        mockMvc.perform(
            post("/api/v1/auth/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signInRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andExpect {
                val signInResponse = objectMapper.readValue(it.response.contentAsString, SignInResponse::class.java)
                assertThat(signInResponse.authToken.value).isNotEmpty
                assertThat(signInResponse.refreshToken.value).isNotEmpty
            }
    }

    @Test
    fun authorizationTest() {
        // Given
        val signInRequest = SignInRequest(USERNAME, PASSWORD)

        // When
        mockMvc.perform(
            post("/api/v1/auth/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signInRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andExpect {
                val signInResponse = objectMapper.readValue(it.response.contentAsString, SignInResponse::class.java)

                mockMvc.perform(
                    get("/")
                        .header("authorization", "Bearer " + signInResponse.authToken.value)
                )
                    // Then
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            }
    }

    @Test
    fun refreshTest() {
        // Given
        val signInRequest = SignInRequest(USERNAME, PASSWORD)

        // When
        mockMvc.perform(
            post("/api/v1/auth/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signInRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andExpect {
                val signInResponse = objectMapper.readValue(it.response.contentAsString, SignInResponse::class.java)
                val refreshRequest = RefreshRequest(signInResponse.refreshToken)

                mockMvc.perform(
                    post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(refreshRequest))
                        .accept(MediaType.APPLICATION_JSON)
                )
                    // Then
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                    .andExpect {
                        val refreshResponse =
                            objectMapper.readValue(it.response.contentAsString, RefreshResponse::class.java)
                        assertThat(refreshResponse.authToken.value).isNotEmpty()
                    }

            }
    }


}