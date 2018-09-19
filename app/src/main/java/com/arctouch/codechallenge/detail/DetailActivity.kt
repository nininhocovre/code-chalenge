package com.arctouch.codechallenge.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Locator
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_content.*

class DetailActivity : AppCompatActivity(), DetailContract {
    private var movieId: Int = 0

    private val movieImageUrlBuilder = MovieImageUrlBuilder()

    companion object {
        const val MOVIE_ID: String = "movie_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        movieId = intent.getIntExtra(MOVIE_ID, -1)
        if (movieId < 0) finish()
    }

    override fun onStart() {
        super.onStart()

        Locator.detailPresenter.viewConnected(movieId, this)
    }

    override fun onStop() {
        super.onStop()

        Locator.detailPresenter.viewDisconnected()
    }

    override fun showMovie(movie: Movie) {
        toolbar.title = movie.title
        genres.text = movie.genres?.joinToString(separator = ", ") { it.name }
        date.text = movie.releaseDate
        overview.text = movie.overview

        Glide.with(this)
                .load(movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(poster)

        Glide.with(this)
                .load(movie.backdropPath?.let { movieImageUrlBuilder.buildBackdropUrl(it) })
                .into(backdrop)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
