package com.unam.appredsocialigalumnos.ui


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unam.appredsocialigalumnos.R
import com.unam.appredsocialigalumnos.api.RetrofitInstance
import com.unam.appredsocialigalumnos.api.SimpleApi
import com.unam.appredsocialigalumnos.databinding.FragmentFeedBinding
import com.unam.appredsocialigalumnos.models.Post
import com.unam.appredsocialigalumnos.models.PostList
import com.unam.appredsocialigalumnos.ui.AdapterPosts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedFragment : FragmentBase<FragmentFeedBinding>(
    R.layout.fragment_feed, FragmentFeedBinding::bind) {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapterPost: RecyclerView.Adapter<AdapterPosts.ViewHolder>? = null
    private var postList = mutableListOf<Post>()
    private lateinit var call : Call<PostList>

    override fun initElements() {
        showCollapsingToolBar(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(activity.applicationContext)
        binding.recyclerViewFeed.layoutManager = layoutManager

        adapterPost = AdapterPosts(postList, activity.applicationContext)
        binding.recyclerViewFeed.adapter = adapterPost
        binding.recyclerViewFeed.setHasFixedSize(true)

        val apiService = RetrofitInstance.api
        call = apiService.getPost("character")
        call.enqueue(object: Callback<PostList>{
            override fun onResponse(call: Call<PostList>, response: Response<PostList>) {
                postList.addAll(response.body()!!.results)
                binding.recyclerViewFeed.adapter?.notifyDataSetChanged()
                }

            override fun onFailure(call: Call<PostList>, t: Throwable) {
                val toast = Toast.makeText(view.context,"ERROR: "+t.message+t.stackTrace+" TRY AGAIN",Toast.LENGTH_LONG)
                toast.show()
                t.message?.let { Log.e("RETROFIT: ", it) }
                t.stackTrace
            }
        })
    }
}