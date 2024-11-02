package com.albertokato.hpapi

import retrofit2.http.GET
import retrofit2.http.Path

interface HpApi {
    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: String): List<HpCharacter>
    @GET("characters/staff")
    suspend fun getStaff(): List<HpCharacter>
    @GET("house/{house}")
    suspend fun getCharactersByHouse(@Path("house") house: String): List<HpCharacter>
}