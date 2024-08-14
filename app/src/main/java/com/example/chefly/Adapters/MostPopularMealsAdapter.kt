package com.example.chefly.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chefly.databinding.PopularItemsBinding
import com.example.chefly.pojo.MealsbyCategory

class MostPopularMealsAdapter() :
    RecyclerView.Adapter<MostPopularMealsAdapter.PopoularMealViewHolder>() {
    lateinit var onItemClick: ((MealsbyCategory) -> Unit)
    var onLongItemClick: ((MealsbyCategory) -> Unit)? = null

    class PopoularMealViewHolder(val binding: PopularItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var mealsList = ArrayList<MealsbyCategory>()
    fun setMeals(mealsList: ArrayList<MealsbyCategory>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopoularMealViewHolder {
        return PopoularMealViewHolder(
            PopularItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: PopoularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mealsList[position])
            true
        }
    }

}