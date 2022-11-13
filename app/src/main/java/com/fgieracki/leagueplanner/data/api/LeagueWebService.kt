package com.fgieracki.leagueplanner.data.api

import com.fgieracki.leagueplanner.data.api.model.LeagueResponseDTO
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object LeagueWebService {
    private val api: LeaguePlannerApi by lazy {
        retrofit.create(LeaguePlannerApi::class.java)
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    init {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://www.themealdb.com/api/json/v1/1/") //TODO: change to LeaguePlannerApi
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        api = retrofit.create(LeaguePlannerApi::class.java)
//    }

    interface LeaguePlannerApi {
        @GET("categories.php")
        suspend fun getLeagues(): Response<LeagueResponseDTO>
    }

    suspend fun getLeagues(): Response<LeagueResponseDTO> {
        return api.getLeagues()
    }


}