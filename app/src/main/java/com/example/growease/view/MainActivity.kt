package com.example.growease.view

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.growease.CategoryFragment
import com.example.growease.PlantData
import com.example.growease.PlantItem
import com.example.growease.R
import com.example.growease.adapter.PlantAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlantAdapter
    private val allPlants by lazy {
        mutableListOf<PlantItem>().apply {
            addAll(PlantData.saksiBitkileri)
            addAll(PlantData.agaclar)
            addAll(PlantData.meyveler)
            addAll(PlantData.sebzeler)
            addAll(PlantData.gubreleme)
            addAll(PlantData.budama)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupSearchView()
        setupRecyclerView()
        setupCardClickListeners()
    }

    private fun setupSearchView() {
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    performSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    performSearch(newText)
                } else {
                    clearSearch()
                }
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.searchResultsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PlantAdapter { plant ->
            showPlantDetail(plant)
        }
        recyclerView.adapter = adapter
    }

    private fun performSearch(query: String) {
        val searchQuery = query.trim().lowercase()
        
        val searchResults = allPlants.filter { plant ->
            val plantName = plant.title.trim().lowercase()
            plantName.startsWith(searchQuery)
        }

        if (searchResults.isNotEmpty()) {
            showSearchResults(searchResults)
        } else {
            clearSearch()
        }
    }

    private fun showSearchResults(results: List<PlantItem>) {
        findViewById<View>(R.id.scrollView).visibility = View.GONE
        findViewById<View>(R.id.searchResultsRecyclerView).visibility = View.VISIBLE
        adapter.submitList(results)
    }

    private fun clearSearch() {
        showMainScreen()
    }

    private fun showPlantDetail(plant: PlantItem) {
        val detailFragment = PlantDetailFragment.newInstance(plant)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupCardClickListeners() {
        val cardIds = listOf(
            R.id.cardCategory1 to "Saksı Bitkileri",
            R.id.cardCategory2 to "Ağaçlar",
            R.id.cardCategory3 to "Meyveler",
            R.id.cardCategory4 to "Sebzeler",
            R.id.cardCategory5 to "Gübreleme",
            R.id.cardCategory6 to "Budama"
        )

        cardIds.forEach { (cardId, category) ->
            findViewById<CardView>(cardId).setOnClickListener {
                showCategoryFragment(category)
            }
        }
    }

    private fun showCategoryFragment(category: String) {
        findViewById<View>(R.id.scrollView).visibility = View.GONE
        findViewById<View>(R.id.fragmentContainer).visibility = View.VISIBLE
        findViewById<View>(R.id.searchView).visibility = View.GONE
        
        val fragment = CategoryFragment.newInstance(category)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            
            // Eğer back stack boşsa ana ekrana dön
            if (supportFragmentManager.backStackEntryCount == 0) {
                showMainScreen()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun showMainScreen() {
        findViewById<View>(R.id.scrollView).visibility = View.VISIBLE
        findViewById<View>(R.id.fragmentContainer).visibility = View.GONE
        findViewById<View>(R.id.searchView).visibility = View.VISIBLE
        findViewById<View>(R.id.searchResultsRecyclerView).visibility = View.GONE
        adapter.submitList(emptyList())
    }
}