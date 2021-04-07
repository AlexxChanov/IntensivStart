package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_details_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie

private const val ARG_PARAM1 = "title"

class MovieDetailsFragment : Fragment() {

    private var param1: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getParcelable<Movie>(ARG_PARAM1)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.movie_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
    }

    private fun setData(){
        movie_details_title.text = param1?.title
//        param1?.rating?.let {
//            movie_details_rating_bar.rating = param1?.rating!!
//        }

        Picasso.get()
            .load(R.drawable.placeholder)
            .into(movie_details_iv)
    }
}
