package com.greengrowapps.shoppinglistapp.core.data.product

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable
import java.util.Date

@JsonIgnoreProperties(ignoreUnknown = true)
class ProductDto : Serializable {

    var id: Long? = null

    
    var name: String? = null
    
}
