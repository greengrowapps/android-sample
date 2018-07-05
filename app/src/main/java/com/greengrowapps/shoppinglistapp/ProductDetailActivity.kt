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
import com.greengrowapps.shoppinglistapp.core.data.product.ProductDto
import kotlinx.android.synthetic.main.activity_product_detail.*
import java.util.*
import com.greengrowapps.shoppinglistapp.core.l18n.EnumLocalization
import com.greengrowapps.shoppinglistapp.viewadapters.ObjectConverterStringAdapter

class ProductDetailActivity : BaseActivity() {

    private var isSaving: Boolean = false
    private lateinit var item: ProductDto
    private var isNew: Boolean = false

    private val pendingDependencies = HashSet<String>()
    
    companion object {
        private const val ITEM_EXTRA = "ItemExtra"

        fun newIntent(from: Context): Intent {
            return Intent(from,ProductDetailActivity::class.java)
        }
        fun editIntent(from: Context, item: ProductDto): Intent {
            val intent = Intent(from,ProductDetailActivity::class.java)
            intent.putExtra(ITEM_EXTRA,item)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        if (intent.hasExtra(ITEM_EXTRA)){
            item = intent.extras.getSerializable(ITEM_EXTRA) as ProductDto
            isNew = false
            populate(item)
        }
        else{
            isNew = true
            item = ProductDto()
        }

        save_button.setOnClickListener { attempSave() }
        

        
    }
  

    private fun populate(item: ProductDto) {

    
    
      input_name.setText(item.name?:"")
      
    

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

        
        input_name.error = null
        
        item.name = input_name.text.toString()
          
        


        var cancel = false
        var focusView: View? = null

        
        if (!isNameValid(item.name)) {
          input_name.error = getString(R.string.error_product_invalid_name)
          focusView = input_name
          cancel = true
        }
        

        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            isSaving=true

            val service = getCore().productService()

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

    private fun onSaveSuccess(ProductDto: ProductDto) {
        isSaving=false
        finish()
    }
    

    private fun isNameValid(field: String?): Boolean {
        //TODO: Replace this with your own logic
        
        return !TextUtils.isEmpty(field)
        
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
