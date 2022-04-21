package com.kvr.user.network

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.requests.UploadRequest
import com.github.kittinunf.fuel.coroutines.awaitObjectResponse
import com.kvr.user.BaseApplication
import com.kvr.user.utils.Constants
import java.io.File

class Network<T : Any> (val javaclassname: Class<T>, val url:String, val body : List<Pair<String, String>>? = null) {

    companion object{
        val TAG = Network::class.java.name
        val somethingWentsWrong = "Something went wrong"
    }

    suspend fun networkCall(includeHeader:Boolean = true, methodType:Int = 0, file: File? = null) : Response<T>{
        Log.e(TAG, "Post Body ${body.toString()}")
        Log.e(TAG, "Post url ${url.toString()}")
        val appPref = BaseApplication.appContext.appPref
        val token = appPref.getString(Constants.ACCESS_TOKEN)
        lateinit var request: Request
        when(methodType){
            0 -> request = Fuel.post(url, parameters = body)
            1 -> request = Fuel.get(url)
            2 -> request = Fuel.delete(url)
            3 -> {
                val uploadRequest = Fuel.upload(url, method = Method.POST, parameters = body)
                if(file != null && file.exists()){
                    val fileDataPart = FileDataPart(file = file, name = "photo", filename = file.name, contentType = "image/*" )
                    uploadRequest.add(fileDataPart)
                }
                request = uploadRequest
            }
        }
        //don't include token for register/login/otp/reset/forgot
        if(token != null && url != "/mobile-login" && url != "/verify-mobile-login" && url != "/signup"  && url != "/verify-otp"  && url != "/resend-otp" && url != "/forgot-password" && url != "/reset-password"){
            request.header(mapOf("Authorization" to "Bearer $token"))
        }
        val (req, response, result) = request.awaitObjectResponse(DeSerializer<T>(javaclassname))
        Log.e(TAG, "networkCall statusCode ${response.statusCode}")
        Log.e(TAG, "networkCall Req ${req.toString()}")
        Log.e(TAG, "networkCall Response ${response.toString()}")
        Log.e(TAG, "networkCall Result ${result.toString()}")
        return if(response.statusCode == 200)  {
            Response.Success(data = result)
        }else {
            Response.Error(error = somethingWentsWrong)
        }
    }
}

