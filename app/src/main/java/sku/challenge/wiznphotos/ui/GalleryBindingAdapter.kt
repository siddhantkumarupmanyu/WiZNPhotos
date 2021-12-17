package sku.challenge.wiznphotos.ui

import android.webkit.WebSettings
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import sku.challenge.wiznphotos.R


@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imgUrl: String) {
    val url = GlideUrl(
        imgUrl, LazyHeaders.Builder()
            .addHeader("User-Agent", WebSettings.getDefaultUserAgent(view.context))
            .build()
    )
    Glide.with(view.context)
        .load(url)
        // .transition()
        .placeholder(R.drawable.no_thumbnail)
        .into(view)
}
