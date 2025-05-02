package com.example.growease

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.growease.adapter.OnboardingAdapter
import com.example.growease.model.OnboardingItem
import com.example.growease.view.MainActivity

class OnboardingActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var indicator1: ImageView
    private lateinit var indicator2: ImageView
    private lateinit var indicator3: ImageView
    private lateinit var nextButton: Button
    private lateinit var skipButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        // Check if it's the first launch
        sharedPreferences = getSharedPreferences("GrowEasePrefs", MODE_PRIVATE)
        if (!isFirstLaunch()) {
            startMainActivity()
            return
        }

        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        indicator1 = findViewById(R.id.indicator1)
        indicator2 = findViewById(R.id.indicator2)
        indicator3 = findViewById(R.id.indicator3)
        nextButton = findViewById(R.id.nextButton)
        skipButton = findViewById(R.id.skipButton)

        val onboardingItems = listOf(
            OnboardingItem(
                R.drawable.onboarding_1,
                "Bitki Bakımı Rehberi",
                "GrowEase ile bitkilerinizin bakımını kolayca öğrenin ve uygulayın."
            ),
            OnboardingItem(
                R.drawable.onboarding_2,
                "Detaylı Bilgiler",
                "Her bitki için su ihtiyacı, ışık gereksinimi ve toprak tipi gibi detaylı bilgilere ulaşın."
            ),
            OnboardingItem(
                R.drawable.onboarding_3,
                "Kategorize Edilmiş İçerik",
                "Saksı bitkileri, ağaçlar, meyveler ve sebzeler gibi kategorilerde bitkileri keşfedin."
            )
        )

        viewPager.adapter = OnboardingAdapter(onboardingItems)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateIndicators(position)
                updateButtons(position)
            }
        })

        nextButton.setOnClickListener {
            if (viewPager.currentItem + 1 < onboardingItems.size) {
                viewPager.currentItem += 1
            } else {
                startMainActivity()
            }
        }

        skipButton.setOnClickListener {
            startMainActivity()
        }
    }

    private fun isFirstLaunch(): Boolean {
        val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)
        if (isFirstLaunch) {
            sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
        }
        return isFirstLaunch
    }

    private fun updateIndicators(position: Int) {
        indicator1.setImageResource(if (position == 0) R.drawable.indicator_active else R.drawable.indicator_inactive)
        indicator2.setImageResource(if (position == 1) R.drawable.indicator_active else R.drawable.indicator_inactive)
        indicator3.setImageResource(if (position == 2) R.drawable.indicator_active else R.drawable.indicator_inactive)
    }

    private fun updateButtons(position: Int) {
        nextButton.text = if (position == 2) "Başla" else "İleri"
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
} 
