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

    private var adapter: HomeAdapter? = null
    private var lastPage: Int = 0
    private var totalPages: Int = 0

    fun viewConnected(view: HomeContract) {
        viewContract = view
        loadMovies(1)
    }

    fun viewDisconnected() {
        viewContract = null
    }

    fun onItemClicked(movie: Movie) {
        viewContract?.startDetailActivity(movie)
    }

    private fun loadMovies(page: Long) {
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                    lastPage = page.toInt()
                    totalPages = it.totalPages
                    if (page == 1L) {
                        viewContract?.let {
                            adapter = HomeAdapter(moviesWithGenres.toMutableList(), loadMore)
                            it.setAdapter(adapter!!)
                            it.showLoading(false)
                        }
                    } else {
                        viewContract?.adapterAddItems(moviesWithGenres)
                    }
                }
    }

    private val loadMore : LoadMoreMovies = object : LoadMoreMovies{
        override fun loadMore() {
            if (lastPage < totalPages) {
                loadMovies((lastPage + 1).toLong())
            } else {
                viewContract?.adapterAddItems(null)
            }
        }
    }
}

interface HomeContract {
    fun setAdapter(adapter: HomeAdapter)
    fun showLoading(show: Boolean)
    fun startDetailActivity(movie: Movie)
    fun adapterAddItems(newMovies: List<Movie>?)
}

interface LoadMoreMovies {
    fun loadMore()
}