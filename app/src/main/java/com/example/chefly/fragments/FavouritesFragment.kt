package com.example.chefly.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.chefly.Adapters.MealsAdapter
import com.example.chefly.activities.MainActivity
import com.example.chefly.databinding.FragmentFavouritesBinding
import com.example.chefly.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavouritesFragment : Fragment() {
private lateinit var binding:FragmentFavouritesBinding
private lateinit var viewModel:HomeViewModel
private lateinit var favouritesAdapter: MealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observeFavourites()
        val itemTouchHelper = object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper .LEFT

        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
val position = viewHolder.adapterPosition
                viewModel.deleteMeal(favouritesAdapter.differ.currentList[position])
                Snackbar.make(requireView(),"Meal Deleted",Snackbar.LENGTH_LONG).setAction(
                    "Undo",View.OnClickListener {
                        viewModel.insertMeal(favouritesAdapter.differ.currentList[position])
                    }
                ).show()
            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavourites)
    }

    private fun prepareRecyclerView() {
favouritesAdapter = MealsAdapter()
        binding.rvFavourites.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = favouritesAdapter
        }
    }

    private fun observeFavourites() {
        //requireactivity
        viewModel.obseveFavouritesMealsLiveData().observe(viewLifecycleOwner, Observer {meals->
meals.forEach{
    favouritesAdapter.differ.submitList(meals)
  //  Log.d("test",it.idMeal)
}
        })
    }

}