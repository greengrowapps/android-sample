package com.greengrowapps.shoppinglistapp.core.data.item

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable
import java.util.Date

@JsonIgnoreProperties(ignoreUnknown = true)
class ItemDto : Serializable {

    var id: Long? = null

    
    var quantity: Double? = null
    
    var productId: Long? = null
    
    var productName: String? = null
    
}
