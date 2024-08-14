package com.example.chefly.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chefly.Adapters.CategoriesAdapter
import com.example.chefly.Adapters.MostPopularMealsAdapter
import com.example.chefly.R
import com.example.chefly.activities.CategoryMealsActivity
import com.example.chefly.activities.MainActivity
import com.example.chefly.activities.MealActivity
import com.example.chefly.databinding.FragmentHomeBinding
import com.example.chefly.fragments.botomsheet.MealBotoomSheetFragment
import com.example.chefly.pojo.MealsbyCategory
import com.example.chefly.pojo.Meal
import com.example.chefly.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularMealsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.chefly.fragments.idMeal"
        const val MEAL_NAME = "com.example.chefly.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.chefly.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.chefly.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
viewModel = (activity as MainActivity).viewModel
//        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
        popularItemsAdapter = MostPopularMealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparePopularItemRecyclerView()
//        viewModel.getRandomMeals()
        observerRandomMeal()
        onRandomMealClick()
        viewModel.getPopularItems()
        observePopularItemLiveData()
        onPopularItemClick()
        preparesCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClicked()
        onPopularItemLongClick()
        onSearchIconClick()
    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick = {meal->
            val mealBotoomSheetFragment = MealBotoomSheetFragment.newInstance(meal.idMeal)
            mealBotoomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClicked() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

    private fun preparesCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
categoriesAdapter.settCategoryList(categories )

        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemRecyclerView() {
        binding.recyclerViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemLiveData() {
        viewModel.observePopularMealLiveData().observe(
            viewLifecycleOwner
        ) { mealList ->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsbyCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {

        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner,
            { meal ->
                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)
                this.randomMeal = meal
            })

    }

}
