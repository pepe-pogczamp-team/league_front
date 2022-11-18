package com.fgieracki.leagueplanner.data.api

import android.util.Log
import com.fgieracki.leagueplanner.data.api.model.*
import com.fgieracki.leagueplanner.data.model.Team
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object LeagueWebService {
    val api: LeaguePlannerApi by lazy {
        retrofit.create(LeaguePlannerApi::class.java)
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    interface LeaguePlannerApi {
        @GET("leagues")
        suspend fun getLeagues(@Header("Authorization") token:String): Response<LeagueResponseDTO>

        @POST("leagues/")
        suspend fun addLeague(@Header("Authorization") token:String,
                              @Body leagueName: AddLeagueDTO): Response<LeagueResponse>

        @GET("teams")
        suspend fun getTeams(@Header("Authorization") token:String,
                             @Query("league") leagueId: Int): Response<TeamResponseDTO>

        @POST("teams/")
        suspend fun addTeam(@Header("Authorization") token:String,
                            @Body team: AddTeamDTO): Response<TeamResponse>
    }


}