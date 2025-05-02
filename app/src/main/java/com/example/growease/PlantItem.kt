package com.example.growease

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlantItem(
    val imageResId: Int,
    val title: String,
    val description: String,
    val waterNeeds: String,
    val lightNeeds: String,
    val soilType: String
) : Parcelable
