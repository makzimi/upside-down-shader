package dev.maxkach.upsidedownsample.sampleproduct

import dev.maxkach.upsidedownsample.R
import dev.maxkach.upsidedownsample.sampleproduct.ProductCardState.ImagesState
import dev.maxkach.upsidedownsample.sampleproduct.ProductCardState.ImagesState.ImageState
import kotlinx.collections.immutable.persistentListOf

object ProductCardStateCreator {
    fun create(
        selectedImage: Int,
    ): ProductCardState {
        return ProductCardState(
            title = "Cozy Cat Hat",
            description = "Welcome to our exclusive collection of feline headwear! Each hat is designed with your sophisticated cat in mind, offering both comfort and undeniable style. Let your beloved companion express their unique personality with a purr-fectly fitted and fashionable accessory!",
            images = ImagesState(
                imagesRes = persistentListOf(
                    ImageState(R.drawable.ic_cat_1),
                    ImageState(R.drawable.ic_cat_2),
                    ImageState(R.drawable.ic_cat_3),
                ),
                currentImage = selectedImage,
            ),
        )
    }
}