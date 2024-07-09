package com.example.projectsummerpractice;

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectsummerpractice.Models.Card

class CardAdapter {private var cardList: MutableList<Card>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    private var filteredCardList = cardList

    override fun onCreateViewHolder(parent:ViewGroup,viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_card, parent, attachToRoot:false )
    }
}
}
