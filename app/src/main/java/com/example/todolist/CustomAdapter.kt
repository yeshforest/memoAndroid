package com.example.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemListBinding

class CustomAdapter(private val memoItems:ArrayList<Memo>, context: Context) : RecyclerView.Adapter<CustomAdapter.ViewHolder>()
{
   private lateinit var myDao:MemoDao
    init{
        myDao=MyDatabase.getDatabase(context).getMemoDao()
    }


    class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item:Memo){
            binding.item.setOnClickListener{

            }

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = ItemListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvTitle.text=memoItems.get(position).title
        holder.binding.tvContent.text=memoItems.get(position).content
        holder.binding.tvDate.text=memoItems.get(position).writeDate
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = memoItems.size
}