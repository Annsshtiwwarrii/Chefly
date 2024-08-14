package com.example.chefly.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.chefly.Adapters.CategoriesAdapter
import com.example.chefly.R
import com.example.chefly.activities.MainActivity
import com.example.chefly.databinding.FragmentCategoriesBinding
import com.example.chefly.viewModel.HomeViewModel

class  CategoriesFragment : Fragment() {
private lateinit var binding:FragmentCategoriesBinding
private  lateinit var categoreiesAdapter:CategoriesAdapter
private lateinit var viewModel:HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observeCategories()
    }

    private fun observeCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer {categories->
            categoreiesAdapter.settCategoryList(categories)
        })
    }

    private fun prepareRecyclerView() {
categoreiesAdapter = CategoriesAdapter()
        binding.recylerViewCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoreiesAdapter
        }
    }
}