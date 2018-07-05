package com.greengrowapps.shoppinglistapp.viewadapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.text.format.DateFormat
import com.greengrowapps.shoppinglistapp.core.l18n.EnumLocalization
import com.greengrowapps.shoppinglistapp.R
import com.greengrowapps.shoppinglistapp.core.data.product.ProductDto

class ProductViewAdapter(private val myDataset: List<ProductDto>, private val editListener: (ProductDto)->Unit, private val deleteListener: (ProductDto)->Unit) :
        RecyclerView.Adapter<ProductViewAdapter.ViewHolder>() {

    class ViewHolder(val parent: View, val editButton: View, val deleteButton: View
                      
                      ,val nameTextView: TextView
                      
    ) : RecyclerView.ViewHolder(parent)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ProductViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_product_item, parent, false) as View

        return ViewHolder(view,view.findViewById(R.id.edit_button),view.findViewById(R.id.delete_button)
          
          , view.findViewById(R.id.tv_name)
          
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = myDataset[position]
        holder.editButton.setOnClickListener{view -> editListener(item) }
        holder.deleteButton.setOnClickListener{view -> deleteListener(item) }

        
        
          holder.nameTextView.text = item.name?:""
          
        
    }

    override fun getItemCount() = myDataset.size
}
