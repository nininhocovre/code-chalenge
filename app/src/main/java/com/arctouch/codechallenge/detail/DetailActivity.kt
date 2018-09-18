package com.arctouch.codechallenge.detail

import android.os.Bundle
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_content.*

class DetailActivity : BaseActivity() {

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

        api.movie(movieId.toLong(), TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    toolbar.title = it.title
                    genres.text = it.genres?.joinToString(separator = ", ") { it.name }
                    date.text = it.releaseDate
                    overview.text = it.overview

                    Glide.with(this)
                            .load(it.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                            .into(poster)

                    Glide.with(this)
                            .load(it.backdropPath?.let { movieImageUrlBuilder.buildBackdropUrl(it) })
                            .into(backdrop)
                }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
