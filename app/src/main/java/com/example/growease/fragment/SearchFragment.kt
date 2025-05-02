package com.example.growease.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.growease.PlantData
import com.example.growease.PlantItem
import com.example.growease.R
import com.example.growease.adapter.PlantAdapter

class SearchFragment : Fragment() {
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultsTextView: TextView
    private lateinit var adapter: PlantAdapter
    private val allPlants = mutableListOf<PlantItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.searchView)
        recyclerView = view.findViewById(R.id.searchResultsRecyclerView)
        noResultsTextView = view.findViewById(R.id.noResultsTextView)

        setupRecyclerView()
        setupSearchView()
        loadAllPlants()
    }

    private fun setupRecyclerView() {
        adapter = PlantAdapter { plant ->
            // Plant detay sayfasına git
            showPlantDetail(plant)
        }
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@SearchFragment.adapter
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterPlants(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterPlants(newText)
                return true
            }
        })
    }

    private fun loadAllPlants() {
        allPlants.clear()
        allPlants.addAll(PlantData.saksiBitkileri)
        allPlants.addAll(PlantData.agaclar)
        allPlants.addAll(PlantData.meyveler)
        allPlants.addAll(PlantData.sebzeler)
        allPlants.addAll(PlantData.gubreleme)
        allPlants.addAll(PlantData.budama)
    }

    private fun filterPlants(query: String?) {
        if (query.isNullOrBlank()) {
            adapter.submitList(emptyList())
            noResultsTextView.visibility = View.GONE
            return
        }

        val filteredList = allPlants.filter { plant ->
            plant.title.contains(query, ignoreCase = true)
        }

        adapter.submitList(filteredList)
        noResultsTextView.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showPlantDetail(plant: PlantItem) {
        // Plant detay sayfasına git
        // TODO: Implement navigation to plant detail
    }
} 