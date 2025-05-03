package com.example.growease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private val items: List<PlantItem>,
    private val onItemClick: (PlantItem) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.itemImage)
        val titleView: TextView = view.findViewById(R.id.itemTitle)
        val descriptionView: TextView = view.findViewById(R.id.itemDescription)
        val waterNeedsView: TextView = view.findViewById(R.id.itemWaterNeeds)
        val lightNeedsView: TextView = view.findViewById(R.id.itemLightNeeds)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.titleView.text = item.title
        holder.descriptionView.text = item.description
        holder.waterNeedsView.text = getShortWaterNeeds(item.waterNeeds)
        holder.lightNeedsView.text = getShortLightNeeds(item.lightNeeds)
        
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }
    
    // Helper functions to get concise display text
    private fun getShortWaterNeeds(waterNeeds: String): String {
        // Extract first few words or just use "Su: " prefix
        return if (waterNeeds.length > 15) {
            waterNeeds.substring(0, 15) + "..."
        } else {
            waterNeeds
        }
    }
    
    private fun getShortLightNeeds(lightNeeds: String): String {
        // Extract first few words or just use "Işık: " prefix
        return if (lightNeeds.length > 15) {
            lightNeeds.substring(0, 15) + "..."
        } else {
            lightNeeds
        }
    }

    override fun getItemCount() = items.size
} 