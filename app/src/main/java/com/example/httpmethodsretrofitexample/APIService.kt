package com.example.httpmethodsretrofitexample

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface APIService {


    /*
       POST METHOD
    */

    // Raw JSON
    @POST("/api/v1/create")
    fun createEmployee(@Body requestBody: RequestBody): Call<ResponseBody>


    // Form Data
    @Multipart
    @POST("/post")
    fun uploadEmployeeData(@PartMap map: HashMap<String?, RequestBody?>): Call<ResponseBody>


    // Encoded URL
    @FormUrlEncoded
    @POST("/post")
    fun createEmployee(@FieldMap params: HashMap<String?, String?>): Call<ResponseBody>


    /*****************************************************************************************************************************************************/


    /*
        GET METHOD
    */

    @GET("/api/v1/employees")
    fun getEmployees(): Call<ResponseBody>


    // Request using @Query (e.g https://reqres.in/api/users?page=2)
    @GET("/api/users")
    fun getEmployees(@Query("page") page: String?): Call<ResponseBody>


    // Request using @Path (e.g https://reqres.in/api/users/53 - This URL is just an example, it's not working)
    @GET("/api/users/{Id}")
    fun getEmployee(@Path("Id") employeeId: String): Call<ResponseBody>


    /*****************************************************************************************************************************************************/

    /*
       PUT METHOD
    */

    @PUT("/api/users/2")
    fun updateEmployee(@Body requestBody: RequestBody): Call<ResponseBody>


    /*****************************************************************************************************************************************************/


    /*
       DELETE METHOD
    */

    @DELETE("/typicode/demo/posts/1")
    fun deleteEmployee(): Call<ResponseBody>


    /*****************************************************************************************************************************************************/

}