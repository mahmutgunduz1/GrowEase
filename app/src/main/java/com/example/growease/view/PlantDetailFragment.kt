package com.example.growease.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.growease.PlantItem
import com.example.growease.R

class PlantDetailFragment : Fragment() {
    private lateinit var plant: PlantItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<PlantItem>("plant")?.let { plantItem ->
            plant = plantItem
            displayPlantDetails(view)
        }
    }

    private fun displayPlantDetails(view: View) {
        view.findViewById<ImageView>(R.id.detailImage).setImageResource(plant.imageResId)
        view.findViewById<TextView>(R.id.detailTitle).text = plant.title
        view.findViewById<TextView>(R.id.detailDescription).text = plant.description
        view.findViewById<TextView>(R.id.detailWaterNeeds).text = plant.waterNeeds
        view.findViewById<TextView>(R.id.detailLightNeeds).text = plant.lightNeeds
        view.findViewById<TextView>(R.id.detailSoilType).text = plant.soilType
    }

    companion object {
        fun newInstance(plant: PlantItem): PlantDetailFragment {
            return PlantDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("plant", plant)
                }
            }
        }
    }
} 