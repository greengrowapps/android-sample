package com.greengrowapps.shoppinglistapp

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.greengrowapps.jhiusers.JhiUsers
import com.greengrowapps.shoppinglistapp.core.Core

import com.greengrowapps.shoppinglistapp.core.messaging.Messager
import com.greengrowapps.shoppinglistapp.core.messaging.MessagerGetListener

open class BaseActivity : AppCompatActivity(), MessagerGetListener{


    private var messager: Messager? = null

    fun getJhiUsers() : JhiUsers{
        return ( application as MyApplication).getJhiUsers()
    }
    fun getCore() : Core{
        return ( application as MyApplication).getCore()
    }

    fun getMessager() {
      ( application as MyApplication).getMessager(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId==android.R.id.home){
            onBackPressed()
        }
        return super.onContextItemSelected(item)
    }


    override fun onResume() {
      super.onResume()
      messager?.let { it.send("/topic/activity",getPageMessage()) }?: run { getMessager() }
    }

    private fun getPageMessage(): String {
      return "{\"page\":\"${this::class.java.simpleName}\"}"
    }

    override fun onMessagerGet(messager: Messager) {
      this.messager = messager
      messager.send("/topic/activity",getPageMessage())
    }

    override fun onMessagerGetError(error: String) {
      //Ignore
    }



}
