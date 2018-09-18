package com.arctouch.codechallenge.home

import android.os.Bundle
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.util.Locator
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : BaseActivity(), HomeContract {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
    }

    override fun onStart() {
        super.onStart()

        Locator.homePresenter.viewConnected(this)
    }

    override fun onStop() {
        super.onStop()

        Locator.homePresenter.viewDisconnected()
    }

    override fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun setAdapter(adapter: HomeAdapter) {
        recyclerView.adapter = adapter
    }
}
