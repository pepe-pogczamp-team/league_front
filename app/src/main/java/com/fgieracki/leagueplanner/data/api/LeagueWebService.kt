package com.fgieracki.leagueplanner.data.api

import com.fgieracki.leagueplanner.data.api.model.LeagueResponse
import com.fgieracki.leagueplanner.data.api.model.LeagueResponseDTO
import com.fgieracki.leagueplanner.data.api.model.TeamResponseDTO
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    interface LeaguePlannerApi {
        @GET("categories.php")
        suspend fun getLeagues(): Response<LeagueResponseDTO> {
            return api.getLeagues()
        }

        @GET("list.php?i=list")
        suspend fun getTeams(): Response<TeamResponseDTO> {
            return api.getTeams()
        }

        @GET("lookup.php?")
        suspend fun getLeague(@Query("i") leagueId: String): Response<LeagueResponse> {
            return api.getLeague(leagueId)
        }
    }


    suspend fun getLeagues(): Response<LeagueResponseDTO> {
        return api.getLeagues()
    }

    suspend fun getTeams(): Response<TeamResponseDTO> {
        return api.getTeams()
    }

    suspend fun getLeague(leagueId: String): Response<LeagueResponse> {
        return api.getLeague(leagueId = leagueId)
    }

}