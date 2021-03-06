package com.mahfuznow.android_assignment.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mahfuznow.android_assignment.R
import com.mahfuznow.android_assignment.adapter.DelegateActivityRVAdapter
import com.mahfuznow.android_assignment.viewmodel.DelegateActivityViewModel


class DelegateActivity : AppCompatActivity() {

    private var listItems: ArrayList<Any> = ArrayList()

    private lateinit var viewModel: DelegateActivityViewModel
    private lateinit var adapter: DelegateActivityRVAdapter
    private lateinit var progressBar: ProgressBar
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var swipeRefreshListener: SwipeRefreshLayout.OnRefreshListener

    private var isLoadCountry = true
    private var isLoadUser = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delegate)

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.delegate)
        actionBar.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this)[DelegateActivityViewModel::class.java]
        viewModel.fetchData(isLoadCountry, isLoadUser)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progress_bar)

        adapter = DelegateActivityRVAdapter(this)
        //items is a field defined in super class of the adapter
        adapter.items = listItems

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        observeLiveData()

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshListener = SwipeRefreshLayout.OnRefreshListener {
            viewModel.reFetchData(isLoadCountry, isLoadUser)
        }
        swipeRefreshLayout.setOnRefreshListener(swipeRefreshListener)
    }


    private fun observeLiveData() {
        viewModel.listItems.observe(
            this
        ) { listItems ->
            this.listItems = listItems
            //Toast.makeText(this, "Data loaded successfully", Toast.LENGTH_SHORT).show()
            setValues()
        }
        viewModel.isErrorCountryLiveData.observe(
            this
        ) { isError ->
            if (isError)
                onError("Country")
        }
        viewModel.isErrorUserLiveData.observe(
            this
        ) { isError ->
            if (isError)
                onError("User")
        }
    }

    private fun onError(msg: String) {
        progressBar.visibility = View.INVISIBLE
        swipeRefreshLayout.isRefreshing = false
        Toast.makeText(this, "Failed to load data $msg's data", Toast.LENGTH_SHORT).show()
    }

    private fun setValues() {
        progressBar.visibility = View.INVISIBLE
        swipeRefreshLayout.isRefreshing = false
        adapter.items = listItems //IMPORTANT
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.delegate_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.menu_item_countries -> {
                item.isChecked = !item.isChecked
                isLoadCountry = item.isChecked
                swipeRefreshLayout.isRefreshing = true
                swipeRefreshListener.onRefresh()
            }
            R.id.menu_item_users -> {
                item.isChecked = !item.isChecked
                isLoadUser = item.isChecked
                swipeRefreshLayout.isRefreshing = true
                swipeRefreshListener.onRefresh()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}