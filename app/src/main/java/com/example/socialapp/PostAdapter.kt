package com.example.socialapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(options: FirestoreRecyclerOptions<Post>, val listener: IPostAdapter) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(options) {
    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)   {
        val postText: TextView = itemView.findViewById(R.id.postTitle)
        val userText: TextView = itemView.findViewById(R.id.userName)
        val createdAt: TextView = itemView.findViewById(R.id.createdAt)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val userImage: ImageView = itemView.findViewById(R.id.userImage)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val viewholder = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
        viewholder.likeButton.setOnClickListener    {
            listener.onLikeClicked(snapshots.getSnapshot(viewholder.adapterPosition).id)
        }
        return viewholder
    }

    override fun onBindViewHolder(p0: PostViewHolder, p1: Int, p2: Post) {
        p0.postText.text = p2.text
        p0.userText.text = p2.createdBy.displayName
        Glide.with(p0.userImage.context).load(p2.createdBy.imageUrl).circleCrop().into(p0.userImage)
        p0.likeCount.text = p2.likedBy.size.toString()
        p0.createdAt.text = Utils.getTimeAgo(p2.createdAt)

        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = p2.likedBy.contains(currentUserId)
        if(isLiked) {
            p0.likeButton.setImageDrawable(ContextCompat.getDrawable(p0.likeButton.context, R.drawable.ic_liked))
        }   else    {
            p0.likeButton.setImageDrawable(ContextCompat.getDrawable(p0.likeButton.context, R.drawable.ic_unliked))
        }
    }
}

interface IPostAdapter    {
    fun onLikeClicked(postId: String)
}