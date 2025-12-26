package dev.maxkach.upsidedownsample.sampleproduct

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ProductCardState(
    val title: String,
    val description: String,
    val images: ImagesState,
) {
    data class ImagesState(
        val imagesRes: ImmutableList<ImageState>,
        val currentImage: Int = 0,
    ) {
        data class ImageState(
            val imageRes: Int,
        )
    }
}
