package com.arctouch.codechallenge.util

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.detail.DetailPresenter
import com.arctouch.codechallenge.home.HomePresenter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class Locator {

    companion object {
        val homePresenter: HomePresenter by lazy { HomePresenter() }
        val detailPresenter: DetailPresenter by lazy { DetailPresenter() }

        val api: TmdbApi = Retrofit.Builder()
                .baseUrl(TmdbApi.URL)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TmdbApi::class.java)
    }
}