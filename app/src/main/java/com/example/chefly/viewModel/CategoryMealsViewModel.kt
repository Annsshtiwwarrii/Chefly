package com.example.chefly.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chefly.pojo.MealsbyCategory
import com.example.chefly.pojo.MealsbyCategoryList
import com.example.chefly.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel:ViewModel() {
    val mealsLiveData = MutableLiveData<List<MealsbyCategory>>()
    fun getMealsByCategpry(categoryName:String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object :Callback<MealsbyCategoryList>{
            override fun onResponse(
                call: Call<MealsbyCategoryList>,
                response: Response<MealsbyCategoryList>
            ) {
response.body()?.let {mealsList->
mealsLiveData.postValue(mealsList.meals)
}
            }

            override fun onFailure(call: Call<MealsbyCategoryList>, t: Throwable) {
Log.e("CategoryMealsViewModel",t.message.toString())
            }
        })
    }
    fun observeMelsLiveData():LiveData<List<MealsbyCategory>>{
        return mealsLiveData
    }
}