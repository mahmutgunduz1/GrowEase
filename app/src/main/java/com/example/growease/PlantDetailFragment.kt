package com.example.growease

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class PlantDetailFragment : Fragment() {
    private var plantItem: PlantItem? = null

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

        plantItem?.let { plant ->
            view.findViewById<ImageView>(R.id.detailImage).setImageResource(plant.imageResId)
            view.findViewById<TextView>(R.id.detailTitle).text = plant.title
            view.findViewById<TextView>(R.id.detailDescription).text = plant.description
            view.findViewById<TextView>(R.id.detailWaterNeeds).text = plant.waterNeeds
            view.findViewById<TextView>(R.id.detailLightNeeds).text = plant.lightNeeds
            view.findViewById<TextView>(R.id.detailSoilType).text = plant.soilType
        }
    }
} 