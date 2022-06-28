package com.unam.appredsocialigalumnos.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.unam.appredsocialigalumnos.R
import com.unam.appredsocialigalumnos.models.Post
import java.security.AccessController.getContext

class AdapterPosts(private val postList: List<Post>, val context: Context): RecyclerView.Adapter<AdapterPosts.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username: TextView
        var image: ImageView
        var post: ImageView
        var comments: TextView
        var likes: TextView
        init{
            username = itemView.findViewById(R.id.username_text)
            image = itemView.findViewById(R.id.user_photo_image)
            post = itemView.findViewById(R.id.post_image)
            comments = itemView.findViewById(R.id.caption_text)
            likes = itemView.findViewById(R.id.likes_text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = postList[position].name
        holder.comments.text = postList[position].gender
        holder.likes.text = postList[position].likes.toString()
        Glide.with(context)
            .load(postList[position].image)
            .circleCrop()
            .into(holder.image)
        Glide.with(context)
            .load(postList[position].image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_instagram)
            .into(holder.post)
    }

    override fun getItemCount(): Int = postList.size


}