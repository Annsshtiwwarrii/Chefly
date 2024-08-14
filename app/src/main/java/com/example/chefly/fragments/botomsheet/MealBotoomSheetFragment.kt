package com.example.chefly.fragments.botomsheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.chefly.R
import com.example.chefly.activities.MainActivity
import com.example.chefly.activities.MealActivity
import com.example.chefly.databinding.FragmentMealBotoomSheetBinding
import com.example.chefly.fragments.HomeFragment
import com.example.chefly.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val MEAL_ID = "param1"

class MealBotoomSheetFragment : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var mealId: String? = null
    private lateinit var binding: FragmentMealBotoomSheetBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBotoomSheetBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealId?.let { viewModel.getMealsById(it) }
        observeBottomSheetMeal()
        onBottomSheetDialogClick()
    }

    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {
if (mealName !=null && mealThumb!=null){
    val intent = Intent(activity,MealActivity::class.java)
    intent.apply {
        putExtra(HomeFragment.MEAL_ID, mealId)
        putExtra(HomeFragment.MEAL_NAME, mealName)
        putExtra(HomeFragment.MEAL_THUMB, mealThumb)
    }
    startActivity(intent)
}
        }
    }

    private var mealName: String? = null
    private var mealThumb: String? = null
    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetMeal().observe(viewLifecycleOwner, Observer { meal ->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgBottomSheet)
            binding.tvBottomSheetArea.text = meal.strArea
            binding.tvBottomSheetCategory.text = meal.strCategory
            binding.bottomsheetMealName.text = meal.strMeal
            mealName = meal.strMeal
            mealThumb = meal.strMealThumb
        })
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            MealBotoomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}