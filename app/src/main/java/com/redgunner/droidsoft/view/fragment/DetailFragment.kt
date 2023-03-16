package com.redgunner.droidsoft.view.fragment

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.text.Html
import android.view.View
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.bumptech.glide.Glide
import com.redgunner.droidsoft.R
import com.redgunner.droidsoft.models.post.Post
import com.redgunner.droidsoft.state.PostState
import com.redgunner.droidsoft.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.flow.collect


class DetailFragment : Fragment(R.layout.fragment_detail) {


    private val viewModel: SharedViewModel by activityViewModels()
    private val navArgs: DetailFragmentArgs by navArgs()


    override fun onStart() {
        super.onStart()
        setUpWebView()
        viewModel.getPostById(navArgs.postId)



        lifecycleScope.launchWhenStarted {
            viewModel.postState.collect { postState ->
                when (postState) {

                    is PostState.Empty -> {
                        viewModel.getPostById(navArgs.postId)

                    }

                    is PostState.Loading -> {
                        shimmerState(isShimmer = true)

                    }

                    is PostState.Success -> {
                        shimmerState(isShimmer = false)

                        showPost(post = postState.post)

                    }

                    is PostState.Error -> {
                        detailShimmerLayout.isVisible = false
                        Toast.makeText(
                            this@DetailFragment.context,
                            postState.message,
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }


            }
        }


    }

    override fun onResume() {
        super.onResume()
        topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        buttonShowComments.setOnClickListener {
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToCommentsFragment(
                    navArgs.postId
                )
            )
        }

    }

    private fun showPost(post: Post) {

        val htmlContent =
            "<!DOCTYPE html> <html> <head> <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n" +
                    "<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n" +
                    "<link href=\"https://fonts.googleapis.com/css2?family=Montserrat&display=swap\" rel=\"stylesheet\"> </head><meta name= viewport content= width=device-width  initial-scale=1.0 > <style>* {    font-family: 'Montserrat', sans-serif;    width: 100%;        margin-inline-start: 0px !important;    margin-inline-end: 0px !important;}figure.wp-block-image.size-large {    margin-block-start: 0em !important;    margin-block-end: 0em !important;}body {    padding: 0 6vw;}img {    display: inline;    height: auto;    max-width: 100%;}video {    display: inline;    width: 100%;}p {    height: auto;    width: 100%;}iframe {    width: 100%}dn-main-dn {    overflow: hidden;    position: relative;    width: 100%;}.dn-header {    margin-top: 25px;    height: 30px;    background-color: #D0D0D0;    display: flex;    justify-content: space-between;}.dn-item {    width: 100%;    display: flex;}div.dn-logo-amz,div.dn-logo-dn,div.dn-logo-geek {}.dn-header img {    height: 25px;    width: auto;}div.dn-part {    font-size: 12px;    color: white;}.entry-content a:not(button),.comment-content a {    -webkit-transition: all .3s cubic-bezier(0.32, 0.74, 0.57, 1);    transition: all .3s cubic-bezier(0.32, 0.74, 0.57, 1);    word-wrap: break-word;    color: var(--g-color);    text-decoration-line: underline;    text-decoration-color: transparent;    -webkit-text-decoration-color: transparent;}.dn-item {    width: 100%;    display: flex;}.entry-content img,.entry-content video,.comment-content img {    max-width: 100%;    height: auto;}.dn-prices {    width: 100%;}table.dn-table {    border-collapse: collapse;    border-spacing: 0 5px;    border: none;    margin-left: 5%;}.entry-content tr {    display: table-row;    vertical-align: middle;}table.dn-table td {    border: none;}element.style {    color: white;}span.dn-plus-offre {    width: 30%;    background: #D0D0D0;    padding: 3px;    border-radius: 5px;    color: white;    display: flex;    justify-content: center;    align-items: center;}.entry-content tbody tr:nth-child(2n),.comment-content tbody tr:nth-child(2n) {    background-color: rgba(0, 0, 0, .025);}tr.dn-hidden {    display: none;}span.dn-offer {    background: #30dd81;    padding: 3px;    border-radius: 5px;    color: white;    display: inline-block;}span.dn-offer a {    text-decoration: none !important;}.dn-item-amz {    display: flex;    justify-content: space-between;}div.dn-offer-amz {    align-self: center;}span.dn-offer-amz {    width : 50%;    background: #f2ca68;    padding: 3px;    border-radius: 5px;    color: white;    display: inline-block;}.dn-amz-title {    max-width: 50%;    align-self: center;}.dn-amz-title h3 {    font-size: small;}.wp-block-table table {    border-collapse: collapse;    width: 100%;}table {    display: table;    border-collapse: separate;    box-sizing: border-box;    text-indent: initial;    border-spacing: 2px;    border-color: grey;}.entry-content tr {    display: table-row;    vertical-align: middle;}.entry-content td {    display: table-cell;    vertical-align: inherit;}table {    width: 100%;    max-width: 100%;    margin-bottom: 2rem;    border-spacing: 0;    border-collapse: collapse;    border-bottom: 1px solid #8882;    border-left: 1px solid #8882;}.wp-block-table td, .wp-block-table th {    border: 1px solid;    padding: .5em;}ul.wp-block-latest-posts__list.aligncenter.wp-block-latest-posts {    padding: 0;    list-style-type: none;}a {    color: #31ad6e;    text-decoration:none;}</style> <body>   ${
                post.content.rendered.replace(
                    "\"",
                    ""
                )
            } </body></html>"

        postWebView.loadDataWithBaseURL(
            null,
            htmlContent,
            "text/html; charset=utf-8",
            "UTF-8",
            null
        )


        if (!post._embedded.wp_FeaturedMedia.isNullOrEmpty()) {
            Glide.with(this@DetailFragment)
                .load(post._embedded.wp_FeaturedMedia[0].source_url)
                .into(postImage)
        }




        postTitle.text = Html.fromHtml(Html.fromHtml(post.title.rendered).toString())
        postCategory.text = post._embedded.wp_Term[0][0].name
    }

    private fun shimmerState(isShimmer: Boolean) {

        if (isShimmer) {
            detailShimmerLayout.isVisible = isShimmer

        } else {
            detailShimmerLayout.isVisible = isShimmer
            coordinatorLayout.isVisible = true


        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {


                    WebSettingsCompat.setForceDark(
                        postWebView.settings,
                        WebSettingsCompat.FORCE_DARK_ON
                    )

                }
            }
            Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {


                    WebSettingsCompat.setForceDark(
                        postWebView.settings,
                        WebSettingsCompat.FORCE_DARK_OFF
                    )

                }
            }
            else -> {
                //
            }
        }

        postWebView.apply {

            this.fitsSystemWindows = true
            this.settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true

            }
            this.setInitialScale(1)


            this.webChromeClient = object : WebChromeClient() {

                private var mCustomView: View? = null
                private var mCustomViewCallback: CustomViewCallback? = null
                private var mOriginalOrientation = 0
                private var mOriginalSystemUiVisibility = 0

                override fun onHideCustomView() {
                    super.onHideCustomView()
                    (activity!!.window.decorView as FrameLayout).removeView(mCustomView)

                    this.mCustomView = null
                    activity!!.window.decorView.setSystemUiVisibility(this.mOriginalSystemUiVisibility)
                    activity!!.requestedOrientation = this.mOriginalOrientation
                    this.mCustomViewCallback?.onCustomViewHidden()
                    this.mCustomViewCallback = null

                }


                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    super.onShowCustomView(view, callback)


                    if (this.mCustomView != null) {
                        onHideCustomView()
                        return
                    }

                    this.mCustomView = view
                    this.mOriginalSystemUiVisibility =
                        activity?.window?.decorView!!.getSystemUiVisibility()
                    this.mOriginalOrientation = activity!!.requestedOrientation
                    this.mCustomViewCallback = callback

                    (activity!!.window.decorView as FrameLayout).addView(
                        mCustomView,
                        FrameLayout.LayoutParams(-1, -1)
                    )
                    activity!!.window.decorView
                        .setSystemUiVisibility(3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)


                }


            }

        }


    }


}