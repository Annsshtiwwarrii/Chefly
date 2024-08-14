package com.example.chefly.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.chefly.R
import com.example.chefly.databinding.ActivityMealBinding
import com.example.chefly.db.MealDatabase
import com.example.chefly.fragments.HomeFragment
import com.example.chefly.pojo.Meal
import com.example.chefly.viewModel.MealViewModel
import com.example.chefly.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var binding: ActivityMealBinding
    private lateinit var youtubeLink:String
    private lateinit var mealMvvm: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]
        getMealInformationFromIntent()
        setImformtionInViews()
        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observeMealDetailLiveData()
        OnYoutubeImageClick()
        onFavouriteClick()
    }

    private fun onFavouriteClick() {
        binding.btnAddToFav.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this,"Meal saved",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun OnYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)

        }
    }
    private var mealToSave:Meal?=null
    private fun observeMealDetailLiveData() {
        mealMvvm.observeMealDetailLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(t: Meal) {
                OnResposnseCase()
                val meal = t
                mealToSave = meal
                binding.tvCategories.text = "Category : ${meal!!.strCategory}"
                binding.tvArea.text = "Area : ${meal!!.strArea}"
                binding.tvInstructionSteps.text = meal.strInstructions
                youtubeLink = meal.strYoutube.toString()
            }

        })
    }

    private fun setImformtionInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collaspingToolbar.title = mealName
        binding.collaspingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collaspingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }

    private fun loadingCase() {
        binding.progressbar.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.INVISIBLE
        binding.tvInstruction.visibility = View.INVISIBLE
        binding.tvCategories.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE


    }

    private fun OnResposnseCase() {
        binding.progressbar.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.tvInstruction.visibility = View.VISIBLE
        binding.tvCategories.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}