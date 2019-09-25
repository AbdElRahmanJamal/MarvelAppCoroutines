package com.marvelapp.coroutines.marvelhome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.marvelapp.coroutines.R
import com.marvelapp.coroutines.frameworks.appnetwork.interceptor.apiFactory
import com.marvelapp.coroutines.frameworks.getCustomPicasso
import com.marvelapp.coroutines.marvelhome.data.GetMarvelHomeUseCase
import com.marvelapp.coroutines.marvelhome.data.MarvelHomeRepository
import com.marvelapp.coroutines.marvelhome.data.entities.Results
import com.marvelapp.coroutines.marvelhome.data.localdatastore.MarvelCharactersDB
import com.marvelapp.coroutines.marvelhome.data.localdatastore.MarvelHomeLocalDataStore
import com.marvelapp.coroutines.marvelhome.data.remotedatastore.MarvelHomeRemoteDataStore
import com.marvelapp.coroutines.marvelhome.presentation.adapter.MarvelCharactersAdapter
import com.marvelapp.coroutines.marvelhome.presentation.mvi.HomePageIntents
import com.marvelapp.coroutines.marvelhome.presentation.mvi.HomePageStates
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.marvel_home_page.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.startKoin


class MarvelHomePage : Fragment() {

    private lateinit var marvelCharactersAdapter: MarvelCharactersAdapter
    private lateinit var marvelHomePageViewModelFactory: MarvelHomePageViewModelFactory

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Picasso.setSingletonInstance(getCustomPicasso(context!!))
        return inflater.inflate(R.layout.marvel_home_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setMarvelCharactersHomeTittle()
        // setHasOptionsMenu(true)
        initRecView()
        startKoin(activity!!.applicationContext, listOf(apiFactory))

        val appDatabase = MarvelCharactersDB(this.context!!).marvelCharactersDao()
        val marvelHomeLocalDataStore = MarvelHomeLocalDataStore(appDatabase)
        val marvelHomeRemoteDataStore = MarvelHomeRemoteDataStore(get())

        marvelHomePageViewModelFactory = MarvelHomePageViewModelFactory(
                GetMarvelHomeUseCase(
                        MarvelHomeRepository(
                                marvelHomeLocalDataStore
                                , marvelHomeRemoteDataStore
                        )
                )
        )

        with(
            ViewModelProviders.of(this, marvelHomePageViewModelFactory)
                        .get(MarvelHomePageViewModel::class.java)
        ) {
            lifecycleScope.launch {
                intents.send(HomePageIntents.OnHomePageStartIntent(limit = 15, offset = 0))
                for (state in homePageState) {
                    when (state) {
                        is HomePageStates.LoadingState -> lottie_loading.visibility = View.VISIBLE
                        is HomePageStates.SuccessState -> displayDataIntoAdapter(state.value)
                        is HomePageStates.ErrorState -> displayErrorMessage(state.exception)
                    }
                }
            }
        }

    }

    private fun displayErrorMessage(exception: Throwable) {
        error_message.text = exception.toString()
        lottie_loading.visibility = View.GONE
    }

    private fun displayDataIntoAdapter(value: List<Results>) {
        marvelCharactersAdapter.setMarvelCharacters(value)
        lottie_loading.visibility = View.GONE
    }

    private fun initRecView() {
        marvelCharactersAdapter = MarvelCharactersAdapter()
        marvel_character_recView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = marvelCharactersAdapter
        }

    }

    private fun setMarvelCharactersHomeTittle() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = null
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }

}
