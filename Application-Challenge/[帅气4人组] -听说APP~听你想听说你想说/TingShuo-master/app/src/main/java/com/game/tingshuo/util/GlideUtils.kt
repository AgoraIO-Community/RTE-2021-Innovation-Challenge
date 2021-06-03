package com.game.tingshuo.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.game.tingshuo.widgets.GlideRoundTransform
import java.util.concurrent.ExecutionException

object GlideUtils {
    fun load(context: Context?, view: ImageView?, url: String?) {
        Glide.with(context!!).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(view!!)
    }

    fun loadCicle(context: Context?, view: ImageView?, url: String?) {
        Glide.with(context!!).load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transform(GlideRoundTransform(context)).into(view!!)
    }

    fun load(context: Context?, view: ImageView?, url: String?, @DrawableRes placeholder: Int) {
        Glide.with(context!!).load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(placeholder).into(view!!)
    }

    fun loadBitmap(context: Context?, url: String?): Bitmap? {
        try {
            return Glide.with(context!!).asBitmap().load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(
                    Target.SIZE_ORIGINAL,
                    Target.SIZE_ORIGINAL
                )
                .get()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }
}