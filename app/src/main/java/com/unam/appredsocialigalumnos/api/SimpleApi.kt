package com.unam.appredsocialigalumnos.api

import com.unam.appredsocialigalumnos.models.Post
import com.unam.appredsocialigalumnos.models.PostList
import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.Call

interface SimpleApi {

    @GET
    fun getPost(@Url url : String) : Call<PostList>

}