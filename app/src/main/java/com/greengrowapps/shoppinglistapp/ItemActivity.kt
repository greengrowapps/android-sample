package com.greengrowapps.shoppinglistapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.greengrowapps.shoppinglistapp.core.data.item.ItemDto
import com.greengrowapps.shoppinglistapp.viewadapters.ItemViewAdapter

import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.content_item.*

class ItemActivity : BaseActivity() {

    companion object {
        fun openIntent(from: Context) : Intent{
            return Intent(from,ItemActivity::class.java)
        }
    }

    private val items = ArrayList<ItemDto>()
    private lateinit var viewAdapter: ItemViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
          startActivity(ItemDetailActivity.newIntent(this))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewManager = LinearLayoutManager(this)
        viewAdapter = ItemViewAdapter(items, {item -> editItem(item) }, {item -> deleteItem(item) })

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        swiperefresh.setOnRefreshListener { refreshList() }
    }

    private fun deleteItem(item: ItemDto) {
      AlertDialog.Builder(this)
        .setMessage(R.string.sure_to_delete)
        .setPositiveButton(R.string.delete) { dialogInterface, i ->
          getCore().itemService().delete(item.id,{ deleteSuccess() }, { code,error -> deleteError(error)})
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

    private fun editItem(item: ItemDto) {
      startActivity(ItemDetailActivity.editIntent(this,item))
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        swiperefresh.isRefreshing = true
        populateItems(
                getCore().itemService()
                        .readList(
                                true,
                                {list: List<ItemDto> -> populateItems(list); dismissLoadingIndicator() },
                                { statusCode: Int, response: String -> showError(); dismissLoadingIndicator() }
                        ))
    }

    private fun dismissLoadingIndicator() {
        swiperefresh.isRefreshing = false
    }

    private fun showError() {
        Toast.makeText(this, getString(R.string.error_getting_items), Toast.LENGTH_SHORT).show()
    }

    private fun populateItems(list: List<ItemDto>) {
        items.clear()
        items.addAll(list)
        viewAdapter.notifyDataSetChanged()
    }


}
