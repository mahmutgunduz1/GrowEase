package com.example.growease.view

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.growease.CategoryFragment
import com.example.growease.PlantData
import com.example.growease.PlantDetailFragment
import com.example.growease.PlantItem
import com.example.growease.R
import com.example.growease.adapter.PlantAdapter
import com.example.growease.fragment.FavoritesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlantAdapter
    
    // Görünümler
    private lateinit var mainContainer: View
    private lateinit var fragmentContainer: View
    
    private val allPlants by lazy {
        mutableListOf<PlantItem>().apply {
            addAll(PlantData.saksiBitkileri)
            addAll(PlantData.agaclar)
            addAll(PlantData.meyveler)
            addAll(PlantData.sebzeler)
            addAll(PlantData.gubreleme)
            addAll(PlantData.budama)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Don't hide ActionBar completely, we need it for the menu
        supportActionBar?.setDisplayShowTitleEnabled(false)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        // View referanslarını al
        mainContainer = findViewById(R.id.mainContainer)
        fragmentContainer = findViewById(R.id.fragmentContainer)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        setupSearchView()
        setupRecyclerView()
        setupCardClickListeners()
        setupOptionsMenu()
        
        // İlk açılışta ana ekranı göster
        showMainScreen()
    }

    private fun setupOptionsMenu() {
        // Add a menu button to the right of SearchView
        val menuButton = ImageView(this)
        // Yeni ikon kullanılıyor
        menuButton.setImageResource(R.drawable.kalp)
        menuButton.setPadding(16, 16, 16, 16)
        
        // Get the search container
        val searchContainer = findViewById<LinearLayout>(R.id.searchContainer)
        
        // Remove the space placeholder and add the menu button
        if (searchContainer != null && searchContainer.childCount > 1) {
            // Remove the space placeholder
            searchContainer.removeViewAt(searchContainer.childCount - 1)
            
            // Add menu button
            val layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(android.R.dimen.app_icon_size),
                resources.getDimensionPixelSize(android.R.dimen.app_icon_size)
            )
            layoutParams.gravity = Gravity.CENTER_VERTICAL
            menuButton.layoutParams = layoutParams
            searchContainer.addView(menuButton)
        }
        
        // Show popup menu when clicked
        menuButton.setOnClickListener { view ->
            showMoreOptionsMenu(view)
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorites -> {
                // Show Favorites screen
                showFavoritesFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun showMoreOptionsMenu(view: View) {
        val popupMenu = android.widget.PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)
        
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_favorites -> {
                    showFavoritesFragment()
                    true
                }
                else -> false
            }
        }
        
        popupMenu.show()
    }
    
    private fun showFavoritesFragment() {
        showFragment(FavoritesFragment.newInstance())
    }

    private fun setupSearchView() {
        searchView = findViewById(R.id.searchView)

        
        // Hint rengini sabit bir renk olarak ayarla (gece-gündüz modu aynı olacak)
        val id = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = searchView.findViewById<TextView>(id)
        textView?.setHintTextColor(Color.parseColor("#757575")) // Gri renk
        
        // Kullanıcının yazdığı metni görünür yap (koyu renk)
        textView?.setTextColor(Color.parseColor("#212121")) // Koyu renk
        
        // Arama ikonunun rengini her iki modda da aynı olacak şekilde ayarla
        val searchIconId = searchView.context.resources.getIdentifier("android:id/search_mag_icon", null, null)
        val searchIcon = searchView.findViewById<ImageView>(searchIconId)
        searchIcon?.setColorFilter(Color.parseColor("#757575")) // İkon için de aynı gri renk
        
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    performSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    performSearch(newText)
                } else {
                    clearSearch()
                }
                return true
            }
        })
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.searchResultsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PlantAdapter { plant ->
            showPlantDetail(plant)
        }
        recyclerView.adapter = adapter
    }

    private fun performSearch(query: String) {
        val searchQuery = query.trim().lowercase()
        
        val searchResults = allPlants.filter { plant ->
            val plantName = plant.title.trim().lowercase()
            val plantDescription = plant.description.trim().lowercase()
            val plantWaterNeeds = plant.waterNeeds.trim().lowercase()
            val plantLightNeeds = plant.lightNeeds.trim().lowercase()
            val plantSoilType = plant.soilType.trim().lowercase()
            
            plantName.contains(searchQuery) || 
            plantDescription.contains(searchQuery) ||
            plantWaterNeeds.contains(searchQuery) ||
            plantLightNeeds.contains(searchQuery) ||
            plantSoilType.contains(searchQuery)
        }

        if (searchResults.isNotEmpty()) {
            showSearchResults(searchResults)
        } else {
            clearSearch()
        }
    }

    private fun showSearchResults(results: List<PlantItem>) {
        findViewById<View>(R.id.scrollView).visibility = View.GONE
        findViewById<View>(R.id.searchResultsRecyclerView).visibility = View.VISIBLE
        adapter.submitList(results)
    }

    private fun clearSearch() {
        findViewById<View>(R.id.scrollView).visibility = View.VISIBLE
        findViewById<View>(R.id.searchResultsRecyclerView).visibility = View.GONE
        adapter.submitList(emptyList())
    }

    private fun showPlantDetail(plant: PlantItem) {
        showFragment(PlantDetailFragment.newInstance(plant))
    }

    private fun setupCardClickListeners() {
        val cardIds = listOf(
            R.id.cardCategory1 to "Saksı Bitkileri",
            R.id.cardCategory2 to "Ağaçlar",
            R.id.cardCategory3 to "Meyveler",
            R.id.cardCategory4 to "Sebzeler",
            R.id.cardCategory5 to "Gübreleme",
            R.id.cardCategory6 to "Budama"
        )

        cardIds.forEach { (cardId, category) ->
            findViewById<CardView>(cardId).setOnClickListener {
                showCategoryFragment(category)
            }
        }
    }

    private fun showCategoryFragment(category: String) {
        showFragment(CategoryFragment.newInstance(category))
    }
    
    private fun showFragment(fragment: Fragment) {
        // Ana içerik konteynırını gizle, fragment konteynırını göster
        mainContainer.visibility = View.GONE
        fragmentContainer.visibility = View.VISIBLE
        
        // Fragment'ı ekle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        // Geri tuşuna basıldığında önce fragment'ı kaldır
        if (supportFragmentManager.backStackEntryCount > 0) {
            // popBackStack'i çağırmadan önce backstack'in durumunu tespit edelim
            val isLastBackStackEntry = supportFragmentManager.backStackEntryCount == 1
            
            if (isLastBackStackEntry) {
                // Son fragment ise, önce ana ekranı göster, sonra fragment'ı kaldır
                showMainScreen()
                supportFragmentManager.popBackStack()
            } else {
                // Başka fragmentlar varsa, sadece fragment'ı kaldır
                supportFragmentManager.popBackStack()
            }
        } else {
            // Ana ekrandayken çıkış dialog'unu göster
            showExitDialog()
        }
    }

    private fun showExitDialog() {
        val builder = android.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert)
        
        // Dialog başlığı ve mesajı
        builder.setTitle("Uygulamadan Çık")
        builder.setMessage("Uygulamadan çıkmak istediğinize emin misiniz?")
        
        // Evet butonu
        builder.setPositiveButton("Evet") { dialog, _ ->
            dialog.dismiss()
            super.onBackPressed()
        }
        
        // Hayır butonu
        builder.setNegativeButton("Hayır") { dialog, _ ->
            dialog.dismiss()
        }
        
        // Dialog görünümünü özelleştir
        val dialog = builder.create()
        dialog.show()
        
        // Buton renklerini ayarla
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#4CAF50"))
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#F44336"))
    }

    private fun showMainScreen() {
        // Fragment konteynırını gizle, ana içerik konteynırını göster
        fragmentContainer.visibility = View.GONE 
        mainContainer.visibility = View.VISIBLE
        
        // Arama sonuçlarını temizle
        adapter.submitList(emptyList())
        
        // Varsayılan olarak ScrollView'ı göster, arama sonuçlarını gizle
        findViewById<View>(R.id.searchResultsRecyclerView).visibility = View.GONE
        findViewById<View>(R.id.scrollView).visibility = View.VISIBLE
    }
}