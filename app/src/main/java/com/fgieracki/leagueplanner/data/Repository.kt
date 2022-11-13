package com.fgieracki.leagueplanner.data

import android.util.Log
import com.fgieracki.leagueplanner.data.api.LeagueWebService
import com.fgieracki.leagueplanner.data.mappers.toLeagueList
import com.fgieracki.leagueplanner.data.model.League

class Repository(private val api: LeagueWebService = LeagueWebService) {
    suspend fun getLeagues(): List<League> {
        val response = api.getLeagues()
        Log.d("REPOSITORY", response.code().toString())
        return if(response.isSuccessful)
            response.body()?.toLeagueList() ?: emptyList()
        else
            emptyList<League>() //TODO: handle errors
    }

}