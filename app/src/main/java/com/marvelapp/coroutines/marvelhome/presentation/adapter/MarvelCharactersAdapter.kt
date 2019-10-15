package com.marvelapp.coroutines.marvelhome.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marvelapp.coroutines.R
import com.marvelapp.coroutines.frameworks.downloadImage
import com.marvelapp.coroutines.marvelhome.data.entities.Results
import kotlinx.android.synthetic.main.marvel_home_ticket.view.*

class MarvelCharactersAdapter : RecyclerView.Adapter<MarvelCharactersAdapter.MarvelCharactersViewHolder>() {

    private var marvelCharacters: MutableList<Results> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarvelCharactersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.marvel_home_ticket, parent, false)
        return MarvelCharactersViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MarvelCharactersViewHolder, position: Int) {
        val marvelCharacter = marvelCharacters[position]
        marvelCharacter.thumbnail?.let {
            val imageURL = marvelCharacter.thumbnail.path + "." + marvelCharacter.thumbnail.extension
            imageURL.replace("http", "https")
            downloadImage(imageURL, holder.charactersImage)
            holder.charactersName.text = marvelCharacter.name ?: marvelCharacter.title
        }
    }

    internal fun setMarvelCharacters(marvelCharacters: List<Results>) {
        this.marvelCharacters.clear()
        this.marvelCharacters.addAll(marvelCharacters)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return marvelCharacters.size
    }

    inner class MarvelCharactersViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val charactersImage: ImageView = itemView.character_image
        val charactersName: TextView = itemView.character_name

    }
}

