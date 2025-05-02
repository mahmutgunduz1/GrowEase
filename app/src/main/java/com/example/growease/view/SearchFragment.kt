package com.example.growease.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.growease.PlantItem
import com.example.growease.R
import com.example.growease.adapter.PlantAdapter

class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlantAdapter
    private var searchResults: List<PlantItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        recyclerView = view.findViewById(R.id.searchResultsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PlantAdapter { plant ->
            showPlantDetail(plant)
        }
        recyclerView.adapter = adapter
        
        // Get search results from arguments
        arguments?.getParcelableArrayList<PlantItem>("searchResults")?.let { results ->
            searchResults = results
            adapter.submitList(results)
        }
    }

    private fun showPlantDetail(plant: PlantItem) {
        val detailFragment = PlantDetailFragment.newInstance(plant)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        fun newInstance(results: List<PlantItem>): SearchFragment {
            return SearchFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("searchResults", ArrayList(results))
                }
            }
        }
    }
} 