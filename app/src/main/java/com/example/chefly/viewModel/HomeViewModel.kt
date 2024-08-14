package com.example.chefly.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chefly.db.MealDatabase
import com.example.chefly.pojo.Category
import com.example.chefly.pojo.CategoryList
import com.example.chefly.pojo.MealsbyCategoryList
import com.example.chefly.pojo.MealsbyCategory
import com.example.chefly.pojo.Meal
import com.example.chefly.pojo.MealList
import com.example.chefly.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

class HomeViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsbyCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private val favouritesLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private val searchMealsLiveData = MutableLiveData<List<Meal>>()
    init {
        getRandomMeals()
    }
    fun getRandomMeals() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    //        Log.d("TEST","meal id ${randomMeal.idMeal} name${randomMeal.strMeal}")
//                    Glide.with(this@HomeFragment)
//                        .load(randomMeal.strMealThumb)
//                        .into(binding.imgRandomMeal)
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Seafood")
            .enqueue(object : Callback<MealsbyCategoryList> {
                override fun onResponse(
                    call: Call<MealsbyCategoryList>,
                    response: Response<MealsbyCategoryList>
                ) {
                    if (response.body() != null) {
                        popularItemsLiveData.value = response.body()!!.meals
                    }
                }

                override fun onFailure(call: Call<MealsbyCategoryList>, t: Throwable) {
                    Log.d("HomeFragment", t.message.toString())
                }

            })
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }
        })

    }

    fun getMealsById(id: String) {
        RetrofitInstance.api.getMealDetail(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { meal ->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }

        })
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun searchMeals(searchQuery: String) =
        RetrofitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealList = response.body()?.meals
                mealList?.let {
                    searchMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }

        })
    fun observeSearchedMealLiveData():LiveData<List<Meal>> = searchMealsLiveData

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun observePopularMealLiveData(): LiveData<List<MealsbyCategory>> {
        return popularItemsLiveData
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun obseveFavouritesMealsLiveData(): LiveData<List<Meal>> {
        return favouritesLiveData
    }

    fun observeBottomSheetMeal(): LiveData<Meal> = bottomSheetMealLiveData
}
