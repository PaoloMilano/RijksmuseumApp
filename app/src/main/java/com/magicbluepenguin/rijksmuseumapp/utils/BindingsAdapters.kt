package com.magicbluepenguin.rijksmuseumapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("image_url")
fun bindImage(view: ImageView, imageUrlString: String?) {
    if (imageUrlString != null) {
        Picasso.get()
            .load(imageUrlString)
            .stableKey(imageUrlString)
            .into(view)
    } else {
        view.setImageDrawable(null)
    }
}
