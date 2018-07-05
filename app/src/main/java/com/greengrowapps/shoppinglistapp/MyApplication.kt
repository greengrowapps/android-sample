package com.greengrowapps.shoppinglistapp

import android.app.Application
import android.content.Context
import com.greengrowapps.ggarest.GgaRest
import com.greengrowapps.jhiusers.JhiUsers
import com.greengrowapps.jhiusers.JhiUsersImpl
import com.greengrowapps.shoppinglistapp.core.CustomSerializer
import com.greengrowapps.shoppinglistapp.core.Core
import com.greengrowapps.shoppinglistapp.core.config.CoreConfiguration

import com.greengrowapps.shoppinglistapp.core.messaging.Messager
import com.greengrowapps.shoppinglistapp.core.messaging.MessagerGetListener
import com.greengrowapps.shoppinglistapp.core.messaging.MessagerFactory
import com.greengrowapps.shoppinglistapp.core.messaging.StrompMessagerFactory

class MyApplication : Application() {

    private lateinit var jhiUsers: JhiUsers

    private lateinit var core: Core

    private lateinit var config: CoreConfiguration


    private var messagerFactory: MessagerFactory? = null


    override fun onCreate() {
        super.onCreate()

        GgaRest.init(this)
        GgaRest.setSerializer(CustomSerializer())

        if(BuildConfig.DEBUG){
            config = CoreConfiguration("http://192.168.1.6:8080")
        }
        else{
            config = CoreConfiguration("https://shoppinglist.greengrowapps.com")
        }

        jhiUsers = JhiUsersImpl.with(this,config.serverUrl,true,getSharedPreferences("JhiUsers", Context.MODE_PRIVATE))
        core = Core(jhiUsers,config,getSharedPreferences("Core", Context.MODE_PRIVATE),CustomSerializer())

        messagerFactory = StrompMessagerFactory(config.serverUrl) { val token = jhiUsers.authToken; if (token.isNullOrEmpty()) { null } else {token}  }

    }

    fun getJhiUsers() : JhiUsers{
        return jhiUsers
    }

    fun getCore() : Core{
        return core
    }

    fun getMessager(listener: MessagerGetListener){
      messagerFactory?.instance(listener)
    }

}
