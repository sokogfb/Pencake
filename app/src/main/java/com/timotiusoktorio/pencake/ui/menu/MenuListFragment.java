package com.timotiusoktorio.pencake.ui.menu;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.data.model.State;
import com.timotiusoktorio.pencake.databinding.FragmentMenuListBinding;
import com.timotiusoktorio.pencake.ui.BaseActivity;
import com.timotiusoktorio.pencake.ui.adapters.ProductsRvAdapter;
import com.timotiusoktorio.pencake.ui.productdetail.ProductDetailActivity;

import java.util.List;

import javax.inject.Inject;

public class MenuListFragment extends Fragment {

    private static final String ARG_CATEGORY_ID = "ARG_CATEGORY_ID";

    @Inject MenuListFragmentViewModelFactory vmFactory;

    private FragmentMenuListBinding binding;
    private ProductsRvAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ProductsRvAdapter(new ProductsRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                startActivity(ProductDetailActivity.newIntent(requireActivity(), product.toJson()));
            }
        });
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        binding.recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) requireActivity()).getComponent().inject(this);

        if (getArguments() == null || getArguments().getString(ARG_CATEGORY_ID) == null) {
            throw new IllegalStateException("Category ID was not sent here as an argument");
        }

        MenuListFragmentViewModel viewModel = ViewModelProviders.of(this, vmFactory).get(MenuListFragmentViewModel.class);
        viewModel.productsLiveData.observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> data) {
                updateData(data);
            }
        });
        viewModel.stateLiveData.observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                updateState(state);
            }
        });

        String categoryId = getArguments().getString(ARG_CATEGORY_ID);
        viewModel.loadProducts(categoryId);
    }

    private void updateData(@Nullable List<Product> data) {
        if (data != null) {
            adapter.updateData(data);
        }
    }

    private void updateState(@Nullable State state) {
        if (state != null) {
            binding.setState(state);
        }
    }

    public static MenuListFragment newInstance(String categoryId) {
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_ID, categoryId);
        MenuListFragment fragment = new MenuListFragment();
        fragment.setArguments(args);
        return fragment;
    }
}