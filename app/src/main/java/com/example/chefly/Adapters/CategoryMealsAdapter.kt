package com.example.chefly.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chefly.databinding.MealItemBinding
import com.example.chefly.pojo.MealsbyCategory

class CategoryMealsAdapter :RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewModel>(){



    private var mealsList = ArrayList<MealsbyCategory>()
    fun setMealsList(mealList:List<MealsbyCategory>){
this.mealsList = mealList as ArrayList<MealsbyCategory>
        notifyDataSetChanged()

    }
    inner class CategoryMealsViewModel(val binding:MealItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
        return CategoryMealsViewModel(
            MealItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
Glide.with(holder.itemView).load(mealsList[position].strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealsList[position].strMeal
    }

    override fun getItemCount(): Int {
 return  mealsList.size
    }
}