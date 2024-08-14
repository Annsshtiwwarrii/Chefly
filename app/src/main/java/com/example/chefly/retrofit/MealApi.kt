package com.example.chefly.retrofit

import com.example.chefly.pojo.CategoryList
import com.example.chefly.pojo.MealsbyCategoryList
import com.example.chefly.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php")
    fun getMealDetail(@Query("i")id:String):Call<MealList>
    @GET("filter.php")
    fun getPopularItems(@Query("c")categoryName:String):Call<MealsbyCategoryList>
    @GET("categories.php")
    fun  getCategories():Call<CategoryList>
    @GET("filter.php")
    fun getMealsByCategory(@Query("c")categoryName: String):Call<MealsbyCategoryList>
    @GET("search.php?")
    fun searchMeals(@Query("s") serachQuery: String):Call<MealList>

}