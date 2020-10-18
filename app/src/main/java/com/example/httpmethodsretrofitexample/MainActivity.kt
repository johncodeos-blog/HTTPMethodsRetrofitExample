package com.example.httpmethodsretrofitexample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        post_button.setOnClickListener { postMethod() }

        get_button.setOnClickListener { getMethod() }

        put_button.setOnClickListener { putMethod() }

        delete_button.setOnClickListener { deleteMethod() }

    }

    private fun postMethod() {

        // Uncomment the one you want to test, and comment the others

        rawJSON()

        // urlEncoded()

        // formData()

    }

    private fun rawJSON() {
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://dummy.restapiexample.com")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("name", "Jack")
        jsonObject.put("salary", "3540")
        jsonObject.put("age", "23")

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get a call
            val call = service.createEmployee(requestBody)

            withContext(Dispatchers.Main) {
                // We're using .enqueue to get asynchronous the response
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        // Note: An HTTP response may still indicate an application-level failure such as a 404 or 500. Call 'response.isSuccessful' to determine if the response indicates success.
                        if (response.isSuccessful) {
                            // Convert raw JSON to pretty JSON using GSON library
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(
                                JsonParser.parseString(
                                    response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                                )
                            )
                            Log.d("Pretty Printed JSON :", prettyJson)

                            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                            intent.putExtra("json_results", prettyJson)
                            this@MainActivity.startActivity(intent)
                        } else {
                            Log.e("RETROFIT_ERROR", response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("RETROFIT_ERROR", t.message ?: "")
                    }

                })
            }
        }
    }

    private fun formData() {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://httpbin.org")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // List of all MIME Types you can upload: https://www.freeformatter.com/mime-types-list.html


        // Get file from assets folder
        val file = getFileFromAssets(this, "lorem_ipsum.txt")

        val fields: HashMap<String?, RequestBody?> = HashMap()
        fields["email"] = ("jack@email.com").toRequestBody("text/plain".toMediaTypeOrNull())
        fields["file\"; filename=\"upload_file.txt\" "] =
            (file).asRequestBody("text/plain".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get a call
            val call = service.uploadEmployeeData(fields)
            withContext(Dispatchers.Main) {
                // We're using .enqueue to get asynchronous the response
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        // Note: An HTTP response may still indicate an application-level failure such as a 404 or 500. Call 'response.isSuccessful' to determine if the response indicates success.
                        if (response.isSuccessful) {
                            // Convert raw JSON to pretty JSON using GSON library
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(
                                JsonParser.parseString(
                                    response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                                )
                            )
                            Log.d("Pretty Printed JSON :", prettyJson)

                            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                            intent.putExtra("json_results", prettyJson)
                            this@MainActivity.startActivity(intent)
                        } else {
                            Log.e("RETROFIT_ERROR", response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("RETROFIT_ERROR", t.message ?: "")
                    }
                })
            }
        }
    }

    private fun urlEncoded() {
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://postman-echo.com")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // Create HashMap with fields
        val params = HashMap<String?, String?>()
        params["name"] = "Jack"
        params["salary"] = "8054"
        params["age"] = "45"

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get a call
            val call = service.createEmployee(params)
            withContext(Dispatchers.Main) {
                // We're using .enqueue to get asynchronous the response
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        // Note: An HTTP response may still indicate an application-level failure such as a 404 or 500. Call 'response.isSuccessful' to determine if the response indicates success.
                        if (response.isSuccessful) {
                            // Convert raw JSON to pretty JSON using GSON library
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(
                                JsonParser.parseString(
                                    response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                                )
                            )
                            Log.d("Pretty Printed JSON :", prettyJson)

                            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                            intent.putExtra("json_results", prettyJson)
                            this@MainActivity.startActivity(intent)
                        } else {
                            Log.e("RETROFIT_ERROR", response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("RETROFIT_ERROR", t.message ?: "")
                    }

                })
            }
        }
    }

    private fun getMethod() {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://dummy.restapiexample.com")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            // Do the GET request and get a call
            /*
             * For @Query: You need to replace the following line with val call = service.getEmployees(2)
             * For @Path: You need to replace the following line with val call = service.getEmployee(53)
             */
            val call = service.getEmployees()
            withContext(Dispatchers.Main) {
                // We're using .enqueue to get asynchronous the response
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>, response: Response<ResponseBody>
                    ) {
                        // Note: An HTTP response may still indicate an application-level failure such as a 404 or 500. Call 'response.isSuccessful' to determine if the response indicates success.
                        if (response.isSuccessful) {
                            // Convert raw JSON to pretty JSON using GSON library
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(
                                JsonParser.parseString(
                                    response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                                )
                            )
                            Log.d("Pretty Printed JSON :", prettyJson)

                            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                            intent.putExtra("json_results", prettyJson)
                            this@MainActivity.startActivity(intent)
                        } else {
                            Log.e("RETROFIT_ERROR", response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("RETROFIT_ERROR", t.message ?: "")
                    }
                })
            }
        }
    }

    private fun putMethod() {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)

        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put("name", "Nicole")
        jsonObject.put("job", "iOS Developer")

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            // Do the PUT request and get a call
            val call = service.updateEmployee(requestBody)

            withContext(Dispatchers.Main) {
                // We're using .enqueue to get asynchronous the response
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        // Note: An HTTP response may still indicate an application-level failure such as a 404 or 500. Call 'response.isSuccessful' to determine if the response indicates success.
                        if (response.isSuccessful) {
                            // Convert raw JSON to pretty JSON using GSON library
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(
                                JsonParser.parseString(
                                    response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                                )
                            )
                            Log.d("Pretty Printed JSON :", prettyJson)

                            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                            intent.putExtra("json_results", prettyJson)
                            this@MainActivity.startActivity(intent)
                        } else {
                            Log.e("RETROFIT_ERROR", response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("RETROFIT_ERROR", t.message ?: "")
                    }

                })
            }
        }
    }

    private fun deleteMethod() {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/")
            .build()

        // Create Service
        val service = retrofit.create(APIService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            // Do the DELETE request
            val call = service.deleteEmployee()
            withContext(Dispatchers.Main) {
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            // Convert raw JSON to pretty JSON using GSON library
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val prettyJson = gson.toJson(
                                JsonParser.parseString(
                                    response.body()?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                                )
                            )
                            Log.d("Pretty Printed JSON :", prettyJson)

                            val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                            intent.putExtra("json_results", prettyJson)
                            this@MainActivity.startActivity(intent)
                        } else {
                            Log.e("RETROFIT_ERROR", response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("RETROFIT_ERROR", t.message ?: "")
                    }
                })
            }
        }
    }


    private fun getFileFromAssets(context: Context, fileName: String): File =
        File(context.cacheDir, fileName)
            .also {
                if (!it.exists()) {
                    it.outputStream().use { cache ->
                        context.assets.open(fileName).use { inputStream ->
                            inputStream.copyTo(cache)
                        }
                    }
                }
            }
}
