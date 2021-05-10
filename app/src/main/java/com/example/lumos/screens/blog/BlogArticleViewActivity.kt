package com.example.lumos.screens.blog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.util.Log
import android.view.Window
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.lumos.R
import com.example.lumos.databinding.ActivityBlogArticleViewBinding
import com.example.lumos.network.dataclasses.blog.BlogPost
import com.example.lumos.utils.Constants

/**
 * Activity to show a WebView for reading Blog posts
 * Object of class [BlogPost] should be passed to load WebView
 */
class BlogArticleViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlogArticleViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //enable transitions for this activity
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Fade()
            exitTransition = Fade()
        }


        binding = ActivityBlogArticleViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWebview()

        val blogPost: BlogPost? = intent.getParcelableExtra("postItem")
        if (blogPost != null) {


            //load URL into webview
            binding.blogArticleWebview.loadUrl("${Constants.BLOG_BASE_URL}/single/${blogPost.id}")

            binding.shareButton.setOnClickListener {
                //construct URL
                val url = "${Constants.BLOG_BASE_URL}/single/${blogPost.id}"
                Log.d(BlogBottomSheetFragment.TAG, url)
                val share = Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, url)
                    putExtra(
                        Intent.EXTRA_TITLE,
                        blogPost.title
                    )//put title of post as title of android share sheet
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

                }, null)
                startActivity(share)
            }
        }


        /**
         * Set Item Click for each item in the menu
         * Scale Up option scales up till 150%
         * Scale down option scales down till 50%
         * Steps of 25%
         * Default option sets back 100%(default value)
         */
        binding.blogAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.scale_up -> {
                    val currentZoom = binding.blogArticleWebview.settings.textZoom
                    //only scale up till 150%
                    binding.blogArticleWebview.settings.textZoom =
                        if (currentZoom < MAX_ZOOM) (currentZoom + 25) else MAX_ZOOM
                    true
                }
                R.id.scale_down -> {
                    //only scale down till 50%
                    val currentZoom = binding.blogArticleWebview.settings.textZoom
                    binding.blogArticleWebview.settings.textZoom =
                        if (currentZoom > MIN_ZOOM) (currentZoom - 25) else MIN_ZOOM
                    true
                }
                R.id.default_scale -> {
                    binding.blogArticleWebview.settings.textZoom = 100
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Function to setup webview and load blog  article from intent data passed to the activity
     *
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebview() {
        val settings = binding.blogArticleWebview.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.textZoom =
            100//set default zoom to 100(just as a precaution otherwise it is 100 by default)


        /**
         *Set custom clients for webview
         * WebChromeClient to update progress as page loads
         */
        binding.blogArticleWebview.apply {
            webViewClient = WebViewClient()
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    binding.blogProgress.progress = newProgress
                    //ensure that progressbar disappears upon page load completion
                    binding.blogProgress.isVisible = newProgress < 100
                }

            }
        }
    }

    //hide webview for better animations
    override fun onBackPressed() {
        binding.blogArticleWebview.isVisible = false
        super.onBackPressed()
    }

    companion object {
        private const val TAG = "BlogArticleViewActivity"
        private const val MAX_ZOOM = 150
        private const val MIN_ZOOM = 50
    }
}