package com.marvelapp.coroutines.marvelhome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.marvelapp.coroutines.R
import com.marvelapp.coroutines.marvelhome.data.GetMarvelHomeUseCase
import com.marvelapp.coroutines.marvelhome.data.MarvelHomeRepository
import com.marvelapp.coroutines.marvelhome.data.entities.Results
import com.marvelapp.coroutines.marvelhome.data.localdatastore.MarvelCharactersDB
import com.marvelapp.coroutines.marvelhome.data.localdatastore.MarvelHomeLocalDataStore
import com.marvelapp.coroutines.marvelhome.data.remotedatastore.MarvelHomeRemoteDataStore
import com.marvelapp.coroutines.marvelhome.presentation.adapter.EndlessOnScrollListener
import com.marvelapp.coroutines.marvelhome.presentation.adapter.MarvelCharactersAdapter
import com.marvelapp.coroutines.marvelhome.presentation.mvi.HomePageIntents
import com.marvelapp.coroutines.marvelhome.presentation.mvi.HomePageStates
import kotlinx.android.synthetic.main.marvel_home_page.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get


class MarvelHomePage : Fragment() {

    private lateinit var marvelCharactersAdapter: MarvelCharactersAdapter
    private lateinit var marvelHomePageViewModelFactory: MarvelHomePageViewModelFactory
    private lateinit var viewModel: MarvelHomePageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.marvel_home_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setMarvelCharactersHomeTittle()
        // setHasOptionsMenu(true)
        initRecView()

        val appDatabase = MarvelCharactersDB(this.context!!).marvelCharactersDao()
        val marvelHomeLocalDataStore = MarvelHomeLocalDataStore(appDatabase)
        val marvelHomeRemoteDataStore = MarvelHomeRemoteDataStore(get())

        marvelHomePageViewModelFactory = MarvelHomePageViewModelFactory(
            GetMarvelHomeUseCase(
                MarvelHomeRepository(
                    marvelHomeLocalDataStore
                    , marvelHomeRemoteDataStore
                )
            ), this
        )
        viewModel = ViewModelProvider(this, marvelHomePageViewModelFactory).get(MarvelHomePageViewModel::class.java)
        with(viewModel) {
            lifecycleScope.launch {
                intents.send(HomePageIntents.OnHomePageStartIntent(limit = 15, offset = 0))
                for (state in homePageState) {
                    when (state) {
                        is HomePageStates.LoadingState -> lottie_loading.visibility = View.VISIBLE
                        is HomePageStates.SuccessState -> displayDataIntoAdapter(state.value)
                        is HomePageStates.ErrorState -> displayErrorMessage(state.exception)
                        is HomePageStates.LoadingMoreCharactersLoadingState -> loading_more_layout.visibility =
                            View.VISIBLE
                        is HomePageStates.LoadingMoreCharactersSuccessState -> displayDataLoadMoreIntoAdapter(state.value)
                        is HomePageStates.LoadingMoreCharactersErrorState -> displayLoadMoreErrorMessage(state.exception)
                    }
                }
            }
        }

    }

    private fun displayLoadMoreErrorMessage(exception: Throwable) {
        loading_more_layout.visibility = View.GONE
    }

    private fun displayDataLoadMoreIntoAdapter(value: List<Results>) {
        marvelCharactersAdapter.setMarvelCharacters(value)
        loading_more_layout.visibility = View.GONE
    }

    private fun displayErrorMessage(exception: Throwable) {
        error_message.visibility = View.VISIBLE
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
            addOnScrollListener(object : EndlessOnScrollListener() {
                override fun onLoadMore() {
                    with(viewModel) {
                        lifecycleScope.launch {
                            intents.send(HomePageIntents.OnEndlessRecyclerViewIntent(offset = adapter!!.itemCount))
                        }
                    }
                }
            })
        }

    }

    private fun setMarvelCharactersHomeTittle() {
        (activity as? AppCompatActivity)?.supportActionBar?.title = null
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = null
    }

}
