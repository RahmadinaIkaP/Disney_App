package binar.academy.challenge_chapter5.data.network.users

import binar.academy.challenge_chapter5.data.model.User
import binar.academy.challenge_chapter5.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserEndPoint {

    @GET("users")
    fun getUsers() : Call<List<UserResponse>>

    @GET("users/{id}")
    fun getCurrentUser(@Path("id") id : String) : Call<UserResponse>

    @POST("users")
    fun register(@Body user : UserResponse) : Call<UserResponse>

    @PUT("users/{id}")
    fun editUserProfile(@Path("id") id : String, @Body user : User) : Call<UserResponse>
}