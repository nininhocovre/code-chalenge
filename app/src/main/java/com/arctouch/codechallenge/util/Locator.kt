package com.arctouch.codechallenge.util

import com.arctouch.codechallenge.home.HomePresenter

class Locator {

    companion object {
        val homePresenter: HomePresenter by lazy { HomePresenter() }
    }
}