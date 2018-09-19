package com.arctouch.codechallenge.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_item.view.*

class HomeAdapter(private val movies: MutableList<Movie>, private val loadMoreMovies: LoadMoreMovies) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var listener: OnMovieClickedListener? = null
    private var hasMoreItems = true

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val movieImageUrlBuilder = MovieImageUrlBuilder()

        fun bind(movie: Movie, listener: OnMovieClickedListener?) {
            itemView.titleTextView.text = movie.title
            itemView.genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
            itemView.releaseDateTextView.text = movie.releaseDate
            itemView.setOnClickListener {
                listener?.onMovieClicked(movie)
            }
            itemView.loading.visibility = View.GONE

            Glide.with(itemView)
                .load(movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(itemView.posterImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = movies.size + if (hasMoreItems) 1 else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < movies.size) {
            holder.bind(movies[position], listener)
        } else if (hasMoreItems) {
            // next items loading
            loadMoreMovies.loadMore()
            holder.itemView.loading.visibility = View.VISIBLE
        }
    }

    fun addMovies(newMovies: List<Movie>?) {
        if (newMovies != null) {
            movies.addAll(newMovies)
        } else {
            hasMoreItems = false
        }
    }

    fun setOnMovieClickedListener(listener: OnMovieClickedListener) {
        this.listener = listener
    }

    interface OnMovieClickedListener {
        fun onMovieClicked(movie: Movie)
    }
}
