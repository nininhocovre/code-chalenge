package com.arctouch.codechallenge.home

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class HomePresenter {

    private val api: TmdbApi = Retrofit.Builder()
            .baseUrl(TmdbApi.URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbApi::class.java)

    private var viewContract:HomeContract? = null

    fun viewConnected(view: HomeContract) {
        viewContract = view
        loadMovies()
    }

    fun viewDisconnected() {
        viewContract = null
    }

    fun onItemClicked(movie: Movie) {
        viewContract?.startDetailActivity(movie)
    }

    private fun loadMovies() {
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                    viewContract?.let {
                        it.setAdapter(HomeAdapter(moviesWithGenres))
                        it.showLoading(false)
                    }
                }
    }
}

interface HomeContract {
    fun setAdapter(adapter: HomeAdapter)
    fun showLoading(show: Boolean)
    fun startDetailActivity(movie: Movie)
}