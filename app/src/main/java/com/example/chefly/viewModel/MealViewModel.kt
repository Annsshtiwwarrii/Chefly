package com.example.chefly.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chefly.db.MealDatabase
import com.example.chefly.pojo.Meal
import com.example.chefly.pojo.MealList
import com.example.chefly.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MealViewModel(
   val mealDatabase:MealDatabase
):ViewModel(

) {
    private var mealDetailLiveData = MutableLiveData<Meal>()
    private var favouritesLiveData = MutableLiveData<List<Meal>>()
    fun getMealDetail(id:String){
        RetrofitInstance.api.getMealDetail(id).enqueue(object :retrofit2.Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body()!=null){
                    mealDetailLiveData.value = response.body()!!.meals[0]
                }
                else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity",t.message.toString())
            }
        })
    }
    fun observeMealDetailLiveData():LiveData<Meal>{
     return   mealDetailLiveData
    }
    fun insertMeal(meal: Meal){
        viewModelScope.launch {
           mealDatabase.mealDao().upsert(meal)
        }
    }

    fun observeFavouriteLiveData():LiveData<List<Meal>>{
        return favouritesLiveData
    }

}