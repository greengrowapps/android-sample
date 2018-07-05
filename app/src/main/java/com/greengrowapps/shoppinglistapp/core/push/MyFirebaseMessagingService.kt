package com.greengrowapps.shoppinglistapp.core.push

import com.greengrowapps.shoppinglistapp.MyApplication
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)

        token?.let{(applicationContext as MyApplication).getCore().sendFirebaseToken(it)}

    }
}
