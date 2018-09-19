package com.arctouch.codechallenge.detail

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Locator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DetailPresenter {

    private var viewContract: DetailContract? = null

    private var movieId: Int = 0
    private var movie: Movie? = null

    fun viewConnected(movieId: Int, view: DetailContract) {
        viewContract = view
        if (this.movieId == movieId && movie != null) {
            viewContract?.showMovie(movie!!)
        } else {
            loadMovie(movieId)
        }
    }

    fun viewDisconnected() {
        viewContract = null
    }

    private fun loadMovie(movieId: Int) {
        Locator.api.movie(movieId.toLong(), TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    movie = it
                    viewContract?.showMovie(it)
                }
    }
}

interface DetailContract {
    fun showMovie(movie: Movie)
}