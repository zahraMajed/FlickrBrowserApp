package com.example.flickrbrowserapp

import android.app.Activity
import android.content.pm.LauncherActivityInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_view.view.*

class RecyclerAdapter (val activity: MainActivity,val photoList:ArrayList<photo>):RecyclerView.Adapter<RecyclerAdapter.itemViewHolder> (){
    class itemViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        return itemViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_view, parent, false
        ))
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        val title= photoList[position].title
        val photoLink= photoList[position].link

        holder.itemView.apply {
            tvTiltle.text=title
            Glide.with(activity).load(photoLink).into(imageView)
            imageView.setOnClickListener(){
                activity.openImg(photoLink)
            }
        }

    }//end onCreateViewHolder()

    override fun getItemCount(): Int = photoList.size


}//