package com.greengrowapps.shoppinglistapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.greengrowapps.shoppinglistapp.core.data.item.ItemDto
import kotlinx.android.synthetic.main.activity_item_detail.*
import java.util.*
import com.greengrowapps.shoppinglistapp.core.l18n.EnumLocalization
import com.greengrowapps.shoppinglistapp.viewadapters.ObjectConverterStringAdapter

import com.greengrowapps.shoppinglistapp.core.data.product.ProductDto
  
class ItemDetailActivity : BaseActivity() {

    private var isSaving: Boolean = false
    private lateinit var item: ItemDto
    private var isNew: Boolean = false

    private val pendingDependencies = HashSet<String>()
    
    private val productList = ArrayList<ProductDto>()
    private lateinit var productAdapter : ArrayAdapter<ProductDto>
    
    companion object {
        private const val ITEM_EXTRA = "ItemExtra"

        fun newIntent(from: Context): Intent {
            return Intent(from,ItemDetailActivity::class.java)
        }
        fun editIntent(from: Context, item: ItemDto): Intent {
            val intent = Intent(from,ItemDetailActivity::class.java)
            intent.putExtra(ITEM_EXTRA,item)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        if (intent.hasExtra(ITEM_EXTRA)){
            item = intent.extras.getSerializable(ITEM_EXTRA) as ItemDto
            isNew = false
            populate(item)
        }
        else{
            isNew = true
            item = ItemDto()
        }

        save_button.setOnClickListener { attempSave() }
        
        productAdapter = ObjectConverterStringAdapter<ProductDto>(this,productList,{ item -> item.name.toString()})
        spinner_productId.adapter = productAdapter
        

        
        loadDependencies()
        
    }
  
    private fun loadDependencies() {
      pendingDependencies.clear()
      showProgress(true)

      
      pendingDependencies.add("Product")
      getCore().productService().readList(
        false,
        { items -> dependencyLoad(items,productList,"Product")},
        {statusCode, response -> dependencyLoadError(response) })
      
    }

    private fun dependencyLoadError(error: String) {
      Toast.makeText(this,R.string.dependency_load_error,Toast.LENGTH_SHORT).show()
      finish()
    }

    private fun <T> dependencyLoad(items: List<T>, target: ArrayList<T>, dependencyName: String) {
      target.clear()
      target.addAll(items)
      pendingDependencies.remove(dependencyName)
      checkDependencies()
    }
    private fun checkDependencies(){
      if(pendingDependencies.isEmpty()){
        save_button.setOnClickListener { attempSave() }
        
          productAdapter.notifyDataSetChanged()
          
        showProgress(false)
      }
    }
    

    private fun populate(item: ItemDto) {

    
    
      input_quantity.setText( item.quantity?.toString()?:"" )
      
    
    
      spinner_productId.setSelection( productList.indexOfFirst { it -> it.id==item.productId } )
      
    

    }

    private fun parseDate(dateTime: String): Date? {
      val date = dateTime.split(' ').firstOrNull()
      val time = dateTime.split(' ').lastOrNull()

      val calendar = Calendar.getInstance()
      calendar.time = DateFormat.getDateFormat(this).parse(date)

      val timeCalendar = Calendar.getInstance()
      timeCalendar.time = DateFormat.getTimeFormat(this).parse(time)

      calendar.set(Calendar.HOUR_OF_DAY,timeCalendar.get(Calendar.HOUR_OF_DAY))
      calendar.set(Calendar.MINUTE,timeCalendar.get(Calendar.MINUTE))
      calendar.set(Calendar.SECOND,timeCalendar.get(Calendar.SECOND))

      return calendar.time
    }

    private fun attempSave() {
        if (isSaving) {
            return
        }

        
        input_quantity.error = null
        
          item.quantity = input_quantity.text.toString().toDoubleOrNull()
          
        
        input_productId.error = null
        
          item.productId = (spinner_productId.selectedItem as? ProductDto)?.id
          
        


        var cancel = false
        var focusView: View? = null

        
        if (!isQuantityValid(item.quantity)) {
          input_quantity.error = getString(R.string.error_item_invalid_quantity)
          focusView = input_quantity
          cancel = true
        }
        
        if (!isProductIdValid(item.productId)) {
          input_productId.error = getString(R.string.error_item_invalid_productId)
          focusView = input_productId
          cancel = true
        }
        

        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            isSaving=true

            val service = getCore().itemService()

            if(isNew) {
                service.create(item,{ saved -> onSaveSuccess(saved) }, {statusCode, response -> onSaveError(response) })
            }
            else{
                service.update(item,{ saved -> onSaveSuccess(saved) }, {statusCode, response -> onSaveError(response) })
            }
        }
    }

    private fun onSaveError(response: String) {
        showProgress(false)
        isSaving=false
        Toast.makeText(this,R.string.save_error,Toast.LENGTH_SHORT).show()
    }

    private fun onSaveSuccess(ItemDto: ItemDto) {
        isSaving=false
        finish()
    }
    

    private fun isQuantityValid(field: Double?): Boolean {
        //TODO: Replace this with your own logic
        
        return field != null
        
    }
    

    private fun isProductIdValid(field: Long?): Boolean {
        //TODO: Replace this with your own logic
        
        return field?:0>0
        
    }
    

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        entity_form.visibility = if (show) View.GONE else View.VISIBLE
        entity_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        entity_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        save_progress.visibility = if (show) View.VISIBLE else View.GONE
        save_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        save_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

}
