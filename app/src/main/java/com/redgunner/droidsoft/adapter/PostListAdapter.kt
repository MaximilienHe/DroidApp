package com.redgunner.droidsoft.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redgunner.droidsoft.R
import com.redgunner.droidsoft.models.post.Post
import kotlinx.android.synthetic.main.post_view_holder_layout.view.*
import java.text.SimpleDateFormat
import kotlin.time.hours

class PostListAdapter(val postClick: (postId: Int) -> Unit) :
    PagingDataAdapter<Post, PostListAdapter.PostViewHolder>(POST_COMPARATOR) {

    inner class PostViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {


        private val image = itemView.PostImage
        private val title = itemView.PostTitle
        private val category = itemView.postCategory
        private val time = itemView.PostTime

        init {

            image.setOnClickListener {
                getItem(adapterPosition)?.let { it1 -> postClick(it1.id) }
            }

            title.setOnClickListener {
                getItem(adapterPosition)?.let { it1 -> postClick(it1.id) }
            }

            category.setOnClickListener {
                getItem(adapterPosition)?.let { it1 -> postClick(it1.id) }
            }

            time.setOnClickListener {
                getItem(adapterPosition)?.let { it1 -> postClick(it1.id) }
            }
        }


        fun bind(post: Post) {


            if (!post._embedded.wp_FeaturedMedia.isNullOrEmpty()) {

                Glide.with(context)
                    .load(post._embedded.wp_FeaturedMedia[0].source_url)
                    .placeholder(ColorDrawable(Color.parseColor("#e2e2e2")))
                    .into(image)

            }

            title.text = Html.fromHtml(Html.fromHtml(post.title.rendered).toString())


            category.text = post._embedded.wp_Term[0][0].name

            val sdFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(post.date).toString()

            val heure = sdFormat[11] + "" + sdFormat[12] + "h" + sdFormat[14] + "" + sdFormat[15]

            val date =sdFormat[8] + "" + sdFormat[9] + "/" + getMonth(sdFormat[4] +""+ sdFormat[5] + sdFormat[6]) + "/" + sdFormat[30] + sdFormat[31] + sdFormat[32] + sdFormat[33]

            val heureDate = heure + " " + date

            time.text = heureDate

            // Log.
        }

        fun getMonth(str : String):String {
            var month = "00"
            if (str == "Jan") {
                month = "01"
            }
            else if(str == "Feb") {
                month = "02"
            }
            else if(str == "Mar") {
                month = "03"
            }
            else if(str == "Apr") {
                month = "04"
            }
            else if(str == "May") {
                month = "05"
            }
            else if(str == "Jun") {
                month = "06"
            }
            else if(str == "Jul") {
                month = "07"
            }
            else if(str == "Aug") {
                month = "08"
            }
            else if(str == "Sep") {
                month = "09"
            }
            else if(str == "Oct") {
                month = "10"
            }
            else if(str == "Nov") {
                month = "11"
            }
            else {
                month = "12"
            }

            return month
        }


    }


    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {

                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_view_holder_layout, parent, false)
        return PostViewHolder(view, parent.context)
    }


}