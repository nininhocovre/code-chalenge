package com.arctouch.codechallenge.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.detail.DetailActivity
import com.arctouch.codechallenge.model.Movie
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
        adapter.setOnMovieClickedListener(object : HomeAdapter.OnMovieClickedListener {
            override fun onMovieClicked(movie: Movie) {
                Locator.homePresenter.onItemClicked(movie)
            }
        } )
        recyclerView.adapter = adapter
    }

    override fun startDetailActivity(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.MOVIE_ID, movie.id)
        startActivity(intent)
    }
}
