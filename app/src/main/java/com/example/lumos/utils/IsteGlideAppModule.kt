package com.example.lumos.utils

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule


@GlideModule
class IsteGlideAppModule: AppGlideModule() {

    //setup caching strategy
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val diskCacheSizeBytes: Int =1024*1024*Constants.DISK_CACHE_SIZE
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes.toLong()))
    }
}