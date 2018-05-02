package com.timotiusoktorio.pencake.ui.favorites;

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

import com.timotiusoktorio.pencake.R;
import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.data.model.State;
import com.timotiusoktorio.pencake.databinding.FragmentFavoritesBinding;
import com.timotiusoktorio.pencake.ui.BaseActivity;
import com.timotiusoktorio.pencake.ui.adapters.ProductsRvAdapter;
import com.timotiusoktorio.pencake.ui.productdetail.ProductDetailActivity;

import java.util.List;

import javax.inject.Inject;

public class FavoritesFragment extends Fragment {

    @Inject FavoritesViewModelFactory vmFactory;

    private FragmentFavoritesBinding binding;
    private ProductsRvAdapter adapter;
    private FavoritesViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
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
        requireActivity().setTitle(R.string.label_favorites);

        viewModel = ViewModelProviders.of(this, vmFactory).get(FavoritesViewModel.class);
        viewModel.stateLiveData.observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                updateState(state);
            }
        });
        viewModel.productsLiveData.observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(@Nullable List<Product> data) {
                updateData(data);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.refreshFavorites();
    }

    private void updateState(@Nullable State state) {
        if (state != null) {
            binding.setState(state);
        }
    }

    private void updateData(@Nullable List<Product> data) {
        if (data != null) {
            adapter.updateData(data);
        }
    }

    public static FavoritesFragment newInstance() {
        Bundle args = new Bundle();
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(args);
        return fragment;
    }
}