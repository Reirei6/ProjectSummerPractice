package com.example.projectsummerpractice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectsummerpractice.Models.Card
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Search : AppCompatActivity() {
    private lateinit var cardAdapter: CardAdapter
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private val cardList = mutableListOf<Card>()
    private val db = FirebaseDatabase.getInstance().getReference("cards")
    private var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        bottomNavigationView = findViewById(R.id.navigation_bar)
        searchView = findViewById(R.id.search_view)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.visibility = View.GONE

        userID = intent.getStringExtra("userID")

        bottomNavigationView.selectedItemId = R.id.menu_search
        recyclerView.layoutManager = LinearLayoutManager(this)
        cardAdapter = CardAdapter(cardList)
        recyclerView.adapter = cardAdapter

        searchView.setIconifiedByDefault(false)
        setupSearchView()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_main -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userID", userID)
                    startActivity(intent)
                    true
                }

                R.id.menu_like -> {
                    val intent = Intent(this, likepage::class.java)
                    intent.putExtra("userID", userID)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        loadCardsFromFirebase()
    }

    private fun loadCardsFromFirebase() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                cardList.clear()
                for (snapshot in dataSnapshot.children) {
                    val card = snapshot.getValue(Card::class.java)
                    card?.let { cardList.add(it) }
                }
                cardAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Search", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    recyclerView.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.VISIBLE
                }
                cardAdapter.filter(newText.orEmpty())
                return true
            }
        })

        searchView.setOnClickListener {
            searchView.isIconified = false
        }
    }
}