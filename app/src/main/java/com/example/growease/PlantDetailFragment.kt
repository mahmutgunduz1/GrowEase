package com.example.growease

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.growease.model.FavoritesManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PlantDetailFragment : Fragment() {
    private var plantItem: PlantItem? = null
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var favoriteButton: FloatingActionButton

    companion object {
        private const val ARG_PLANT = "plant"

        fun newInstance(plant: PlantItem): PlantDetailFragment {
            return PlantDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PLANT, plant)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        plantItem = arguments?.getParcelable(ARG_PLANT)
        favoritesManager = FavoritesManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Favorite button setup
        favoriteButton = view.findViewById(R.id.favoriteButton)
        updateFavoriteButtonState()
        
        favoriteButton.setOnClickListener {
            toggleFavorite()
        }
        
        // Ağır işlemleri delay ile çalıştır (UI thread'inin düzgün çalışması için)
        view.post {
            bindPlantData(view)
        }
    }
    
    private fun toggleFavorite() {
        plantItem?.let { plant ->
            val isNowFavorite = favoritesManager.toggleFavorite(plant)
            updateFavoriteButtonState()
            
            // Show toast message
            val message = if (isNowFavorite) {
                "Favorilere eklendi"
            } else {
                "Favorilerden çıkarıldı"
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun updateFavoriteButtonState() {
        plantItem?.let { plant ->
            val isFavorite = favoritesManager.isInFavorites(plant)
            if (isFavorite) {
                favoriteButton.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                favoriteButton.setImageResource(R.drawable.ic_favorite_border)
            }
        }
    }
    
    private fun bindPlantData(view: View) {
        if (!isAdded) return  // Fragment geçerli değilse çık
        
        plantItem?.let { plant ->
            try {
                view.findViewById<ImageView>(R.id.detailImage).setImageResource(plant.imageResId)
                view.findViewById<TextView>(R.id.detailTitle).text = plant.title
                view.findViewById<TextView>(R.id.detailDescription).text = plant.description
                view.findViewById<TextView>(R.id.detailWaterNeeds).text = plant.waterNeeds
                view.findViewById<TextView>(R.id.detailLightNeeds).text = plant.lightNeeds
                view.findViewById<TextView>(R.id.detailSoilType).text = plant.soilType
            } catch (e: Exception) {
                // Herhangi bir hata durumunda logcat'e yazdırma
                e.printStackTrace()
            }
        }
    }
} 