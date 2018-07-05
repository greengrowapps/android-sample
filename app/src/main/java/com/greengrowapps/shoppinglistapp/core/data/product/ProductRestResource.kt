package com.greengrowapps.shoppinglistapp.core.data.product

import com.greengrowapps.ggarest.Webservice
import com.greengrowapps.ggarest.listeners.OnListResponseListener
import com.greengrowapps.ggarest.listeners.OnObjResponseListener
import com.greengrowapps.ggarest.listeners.OnResponseListener
import com.greengrowapps.jhiusers.JhiUsers

class ProductRestResource(private val url:String, private val webservice: Webservice, private val users: JhiUsers){

    companion object {
        const val resourceUrl = "api/products"
    }

    fun readList(success: (List<ProductDto>) -> Unit, error: (statusCode:Int, response:String) -> Unit){

        webservice.get("$url/$resourceUrl")
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer ${users.authToken}")
                .onSuccess(ProductDto::class.java, OnListResponseListener{ code, items, fullResponse -> success(items) })
                .onOther(OnResponseListener{code, fullResponse, e -> error(code, fullResponse.toString()) })
                .execute()
    }
    fun readOne(id:Int, success: (ProductDto) -> Unit, error: (statusCode:Int, response:String) -> Unit){

        webservice.get("$url/$resourceUrl/$id")
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer ${users.authToken}")
                .onSuccess(ProductDto::class.java, OnObjResponseListener{ code, item, fullResponse -> success(item) })
                .onOther(OnResponseListener{code, fullResponse, e -> error(code, fullResponse.toString()) })
                .execute()
    }
    fun save(toSave: ProductDto, success: (ProductDto) -> Unit, error: (statusCode:Int, response:String) -> Unit){

        webservice.post("$url/$resourceUrl")
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer ${users.authToken}")
                .withBody(toSave)
                .onResponse(ProductDto::class.java, 201, OnObjResponseListener{ code, item, fullResponse -> success(item) })
                .onOther(OnResponseListener{code, fullResponse, e -> error(code, fullResponse.toString()) })
                .execute()
    }
    fun update(toUpdate: ProductDto, success: (ProductDto) -> Unit, error: (statusCode:Int, response:String) -> Unit){

        webservice.put("$url/$resourceUrl")
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer ${users.authToken}")
                .withBody(toUpdate)
                .onSuccess(ProductDto::class.java, OnObjResponseListener{ code, item, fullResponse -> success(item) })
                .onOther(OnResponseListener{code, fullResponse, e -> error(code, fullResponse.toString()) })
                .execute()
    }
    fun delete(id: Long, success: () -> Unit, error: (statusCode:Int, response:String) -> Unit){

        webservice.delete("$url/$resourceUrl/$id")
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer ${users.authToken}")
                .onSuccess({ code, fullResponse, e -> success() })
                .onOther(OnResponseListener{code, fullResponse, e -> error(code, fullResponse.toString()) })
                .execute()
    }
}
