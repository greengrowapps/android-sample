package com.greengrowapps.shoppinglistapp.core.data.product

import com.greengrowapps.shoppinglistapp.core.cache.CombinedCache

class ProductService(private val resource: ProductRestResource, private val cache: CombinedCache) {

    companion object {
        private const val CACHE_KEY = "get:product:list"
    }

    fun readList(useCache: Boolean, success: (List<ProductDto>) -> Unit, error: (statusCode: Int, response: String) -> Unit) : List<ProductDto>{

        var items : Array<ProductDto>? = null
        if(useCache) {
            items = cache.load(CACHE_KEY, java.lang.reflect.Array.newInstance(ProductDto::class.java, 0).javaClass) as? Array<ProductDto>?
        }
        resource.readList({list -> cache.save(CACHE_KEY,list); success(list) },error)

        if(items!=null){
            return items.toList()
        }
        return ArrayList()
    }
    fun create(item: ProductDto, success: (ProductDto) -> Unit, error: (statusCode: Int, response: String) -> Unit){
      resource.save(item,success,error)
    }
    fun update(item: ProductDto, success: (ProductDto) -> Unit, error: (statusCode: Int, response: String) -> Unit){
      resource.update(item,success,error)
    }
    fun delete(id: Long?, success: () -> Unit, error: (statusCode: Int, response: String) -> Unit) {
      resource.delete(id?:0, success, error)
    }
}
