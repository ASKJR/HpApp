package com.albertokato.hpapi

import retrofit2.http.GET
import retrofit2.http.Path

interface HpApi {
    @GET("/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): HpCharacter
    @GET("/staff")
    suspend fun getStaff(): List<HpCharacter>
    @GET("/house/{house}")
    suspend fun getCharactersByHouse(@Path("house") house: String): List<HpCharacter>
}