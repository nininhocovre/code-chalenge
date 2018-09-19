package com.arctouch.codechallenge.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.detail.DetailActivity
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Locator
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity(), HomeContract {

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

    override fun adapterAddItems(newMovies: List<Movie>?) {
        if (recyclerView.adapter is HomeAdapter) {
            val adapter = recyclerView.adapter as HomeAdapter
            val last = adapter.itemCount
            val count = if (newMovies != null) newMovies.size else 0
            adapter.addMovies(newMovies)
            recyclerView.post {
                adapter.notifyItemChanged(last - 1)
                if (count > 0) {
                    adapter.notifyItemRangeInserted(last, count)
                }
            }
        }
    }

    override fun startDetailActivity(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.MOVIE_ID, movie.id)
        startActivity(intent)
    }
}
