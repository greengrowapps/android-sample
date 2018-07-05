package com.greengrowapps.shoppinglistapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.greengrowapps.shoppinglistapp.core.data.product.ProductDto
import com.greengrowapps.shoppinglistapp.viewadapters.ProductViewAdapter

import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.content_product.*

class ProductActivity : BaseActivity() {

    companion object {
        fun openIntent(from: Context) : Intent{
            return Intent(from,ProductActivity::class.java)
        }
    }

    private val items = ArrayList<ProductDto>()
    private lateinit var viewAdapter: ProductViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
          startActivity(ProductDetailActivity.newIntent(this))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewManager = LinearLayoutManager(this)
        viewAdapter = ProductViewAdapter(items, {item -> editItem(item) }, {item -> deleteItem(item) })

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        swiperefresh.setOnRefreshListener { refreshList() }
    }

    private fun deleteItem(item: ProductDto) {
      AlertDialog.Builder(this)
        .setMessage(R.string.sure_to_delete)
        .setPositiveButton(R.string.delete) { dialogInterface, i ->
          getCore().productService().delete(item.id,{ deleteSuccess() }, { code,error -> deleteError(error)})
        }
        .setNegativeButton(R.string.cancel,null)
        .show()
    }

    private fun deleteError(error: String) {
      Toast.makeText(this,R.string.delete_error,Toast.LENGTH_SHORT).show()
    }

    private fun deleteSuccess() {
      refreshList()
    }

    private fun editItem(item: ProductDto) {
      startActivity(ProductDetailActivity.editIntent(this,item))
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        swiperefresh.isRefreshing = true
        populateItems(
                getCore().productService()
                        .readList(
                                true,
                                {list: List<ProductDto> -> populateItems(list); dismissLoadingIndicator() },
                                { statusCode: Int, response: String -> showError(); dismissLoadingIndicator() }
                        ))
    }

    private fun dismissLoadingIndicator() {
        swiperefresh.isRefreshing = false
    }

    private fun showError() {
        Toast.makeText(this, getString(R.string.error_getting_items), Toast.LENGTH_SHORT).show()
    }

    private fun populateItems(list: List<ProductDto>) {
        items.clear()
        items.addAll(list)
        viewAdapter.notifyDataSetChanged()
    }


}
