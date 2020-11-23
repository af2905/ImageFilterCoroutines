package com.github.af2905.imagefiltercoroutines

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.af2905.imagefiltercoroutines.databinding.FragmentFirstBinding
import com.github.af2905.imagefiltercoroutines.filters.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class FirstFragment : Fragment(), ThumbnailsAdapter.ThumbnailsAdapterListener {
    private var binding: FragmentFirstBinding? = null
    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("FirstFragment", "$exception handled!")
    }
    private val coroutineScope = CoroutineScope(Dispatchers.Main + handler)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstBinding.bind(view)

        coroutineScope.launch(Dispatchers.Main) {
            val originalBitmap =
                ImageLoader.getOriginalBitmapAsync("https://image.tmdb.org/t/p/w342/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg")
            loadImage(originalBitmap)
        }

        fillAdapter()
        binding?.fab?.let {
            it.setOnClickListener { view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }
    }

    private fun fillAdapter() {
        binding?.recyclerView?.adapter = ThumbnailsAdapter(
            listOf(
                FilterItem(
                    previewUrl = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    imageUrl = "https://image.tmdb.org/t/p/w342/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    filterName = "Normal",
                    filter = null
                ),
                FilterItem(
                    previewUrl = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    imageUrl = "https://image.tmdb.org/t/p/w342/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    filterName = "Blur",
                    filter = Blur()
                ),
                FilterItem(
                    previewUrl = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    imageUrl = "https://image.tmdb.org/t/p/w342/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    filterName = "Sketch",
                    filter = Sketch()
                ),
                FilterItem(
                    previewUrl = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    imageUrl = "https://image.tmdb.org/t/p/w342/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    filterName = "GrayScale",
                    filter = GrayScale()
                ),
                FilterItem(
                    previewUrl = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    imageUrl = "https://image.tmdb.org/t/p/w342/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    filterName = "Invert",
                    filter = Invert()
                ),
                FilterItem(
                    previewUrl = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    imageUrl = "https://image.tmdb.org/t/p/w342/aMpyrCizvSdc0UIMblJ1srVgAEF.jpg",
                    filterName = "Vignete",
                    filter = Vignete()
                )
            ), this
        )
    }

    override fun onFilterSelected(filter: IFilter?, url: String) {
        coroutineScope.launch(Dispatchers.Main) {
            binding?.let {
                it.progressBar.visibility = View.VISIBLE
                val originalBitmap = ImageLoader.getOriginalBitmapAsync(url)
                if (filter != null) {
                    val showFilterBitmap = applyFilterAsync(originalBitmap, filter)
                    loadImage(showFilterBitmap)
                } else {
                    loadImage(originalBitmap)
                }
            }
        }
    }

    private suspend fun applyFilterAsync(
        originalBitmap: Bitmap, filter: IFilter? = Blur()
    ): Bitmap =
        withContext(Dispatchers.Default) {
            filter?.apply(originalBitmap)!!
        }

    private fun loadImage(source: Bitmap) {
        binding?.let {
            it.progressBar.visibility = View.GONE
            it.placeHolderImageview.setImageBitmap(source)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}