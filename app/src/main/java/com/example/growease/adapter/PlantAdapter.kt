package com.example.growease.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.growease.PlantItem
import com.example.growease.R


class PlantAdapter(private val onItemClick: (PlantItem) -> Unit) :
    ListAdapter<PlantItem, PlantAdapter.PlantViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plant, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.plantImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.plantNameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.plantDescriptionTextView)
        private val waterNeedsTextView: TextView = itemView.findViewById(R.id.plantWaterNeedsTextView)
        private val lightNeedsTextView: TextView = itemView.findViewById(R.id.plantLightNeedsTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(plant: PlantItem) {
            imageView.setImageResource(plant.imageResId)
            nameTextView.text = plant.title
            descriptionTextView.text = plant.description
            waterNeedsTextView.text = plant.waterNeeds
            lightNeedsTextView.text = plant.lightNeeds
        }
    }

    private class PlantDiffCallback : DiffUtil.ItemCallback<PlantItem>() {
        override fun areItemsTheSame(oldItem: PlantItem, newItem: PlantItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: PlantItem, newItem: PlantItem): Boolean {
            return oldItem == newItem
        }
    }
} 