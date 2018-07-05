package com.greengrowapps.shoppinglistapp.core.data.item

import com.greengrowapps.shoppinglistapp.core.cache.CombinedCache

class ItemService(private val resource: ItemRestResource, private val cache: CombinedCache) {

    companion object {
        private const val CACHE_KEY = "get:item:list"
    }

    fun readList(useCache: Boolean, success: (List<ItemDto>) -> Unit, error: (statusCode: Int, response: String) -> Unit) : List<ItemDto>{

        var items : Array<ItemDto>? = null
        if(useCache) {
            items = cache.load(CACHE_KEY, java.lang.reflect.Array.newInstance(ItemDto::class.java, 0).javaClass) as? Array<ItemDto>?
        }
        resource.readList({list -> cache.save(CACHE_KEY,list); success(list) },error)

        if(items!=null){
            return items.toList()
        }
        return ArrayList()
    }
    fun create(item: ItemDto, success: (ItemDto) -> Unit, error: (statusCode: Int, response: String) -> Unit){
      resource.save(item,success,error)
    }
    fun update(item: ItemDto, success: (ItemDto) -> Unit, error: (statusCode: Int, response: String) -> Unit){
      resource.update(item,success,error)
    }
    fun delete(id: Long?, success: () -> Unit, error: (statusCode: Int, response: String) -> Unit) {
      resource.delete(id?:0, success, error)
    }
}
