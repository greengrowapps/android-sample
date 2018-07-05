package com.greengrowapps.shoppinglistapp.viewadapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.text.format.DateFormat
import com.greengrowapps.shoppinglistapp.core.l18n.EnumLocalization
import com.greengrowapps.shoppinglistapp.R
import com.greengrowapps.shoppinglistapp.core.data.item.ItemDto

class ItemViewAdapter(private val myDataset: List<ItemDto>, private val editListener: (ItemDto)->Unit, private val deleteListener: (ItemDto)->Unit) :
        RecyclerView.Adapter<ItemViewAdapter.ViewHolder>() {

    class ViewHolder(val parent: View, val editButton: View, val deleteButton: View
                      
                      ,val quantityTextView: TextView
                      
                      ,val productIdTextView: TextView
                      
                      ,val productNameTextView: TextView
                      
    ) : RecyclerView.ViewHolder(parent)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ItemViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_item_item, parent, false) as View

        return ViewHolder(view,view.findViewById(R.id.edit_button),view.findViewById(R.id.delete_button)
          
          , view.findViewById(R.id.tv_quantity)
          
          , view.findViewById(R.id.tv_productId)
          
          , view.findViewById(R.id.tv_productName)
          
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = myDataset[position]
        holder.editButton.setOnClickListener{view -> editListener(item) }
        holder.deleteButton.setOnClickListener{view -> deleteListener(item) }

        
        
          holder.quantityTextView.text = item.quantity?.toString()?:""
          
        
        
          holder.productIdTextView.text = item.productId?.toString()?:""
          
        
        
          holder.productNameTextView.text = item.productName?:""
          
        
    }

    override fun getItemCount() = myDataset.size
}
