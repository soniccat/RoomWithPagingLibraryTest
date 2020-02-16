package com.sample.roomandpaginlibrary.net

import retrofit2.http.GET
import retrofit2.http.Query

interface LingvoLiveService {
    @GET("api/v1/Search")
    suspend fun search(@Query("text") text: String,
                       @Query("srcLang") srcLang: Int,
                       @Query("dstLang") dstLang: Int,
                       @Query("searchZone") searchZone: Int,
                       @Query("startIndex") startIndex: Int,
                       @Query("pageSize") pageSize: Int): LingvoSearchResult
}