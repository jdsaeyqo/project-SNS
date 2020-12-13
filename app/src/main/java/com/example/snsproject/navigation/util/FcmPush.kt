package com.example.snsproject.navigation.util

import com.example.snsproject.navigation.model.PushDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class FcmPush {
    var JSON = MediaType.parse("application/json; charset=utf-8")
    var url = "https://fcm.googleapis.com/fcm/send"
    var serverKey = "AAAAckNroDo:APA91bE1l65zSq0NdMSUxfRaKIhhaGM5Xr3vOfktQZcswgGmzER1m1zACWQrG43eQ5ToqSJ_7__u5rPFLdGhHE_wWwnVJ6Ubybv6Ce490a4KptFAymsbX3QtwbsmuJiN02JWPs7247Fi"
    var gson: Gson? = null
    var okhttpClient: OkHttpClient? = null

    companion object {
        var instance = FcmPush()

    }

    init {
        gson = Gson()
        okhttpClient = OkHttpClient()
    }

    fun sendMessage(destination: String, title: String, message: String) {
        FirebaseFirestore.getInstance().collection("pushtokens").document(destination).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var token = task?.result?.get("pushToken").toString()

                    var pushDTO = PushDTO()
                    pushDTO.to = token
                    pushDTO.notification.title = title
                    pushDTO.notification.body = message

                    var body = RequestBody.create(JSON, gson!!.toJson(pushDTO))
                    var request = Request.Builder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "key="+serverKey)
                        .url(url)
                        .post(body)
                        .build()

                    okhttpClient?.newCall(request)?.enqueue(object : Callback {
                        override fun onFailure(call: Call?, e: IOException?) {

                        }

                        override fun onResponse(call: Call?, response: Response?) {
                            println(response?.body()?.string())
                        }

                    })
                }
            }
    }

}