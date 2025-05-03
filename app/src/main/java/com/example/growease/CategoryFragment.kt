package com.example.growease

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private var categoryType: String? = null

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): CategoryFragment {
            return CategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY, category)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryType = arguments?.getString(ARG_CATEGORY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        recyclerView = view.findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        
        // Seçilen kategori için tüm öğeleri getir
        val items = when (categoryType) {
            "Saksı Bitkileri" -> PlantData.saksiBitkileri
            "Ağaçlar" -> PlantData.agaclar
            "Meyveler" -> PlantData.meyveler
            "Sebzeler" -> PlantData.sebzeler
            "Gübreleme" -> PlantData.gubreleme
            "Budama" -> PlantData.budama
            else -> emptyList()
        }
        
        adapter = CategoryAdapter(items) { plant ->
            showPlantDetail(plant)
        }
        recyclerView.adapter = adapter
    }

    private fun showPlantDetail(plant: PlantItem) {
        val fragment = PlantDetailFragment.newInstance(plant)
        
        // Fragment işlemlerini gerçekleştir
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
} 