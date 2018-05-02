package com.timotiusoktorio.pencake.ui.menu;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timotiusoktorio.pencake.R;
import com.timotiusoktorio.pencake.data.model.Category;
import com.timotiusoktorio.pencake.data.model.State;
import com.timotiusoktorio.pencake.databinding.FragmentMenuBinding;
import com.timotiusoktorio.pencake.ui.BaseActivity;
import com.timotiusoktorio.pencake.ui.cart.CartActivity;

import java.util.List;

import javax.inject.Inject;

public class MenuFragment extends Fragment {

    @Inject MenuFragmentViewModelFactory vmFactory;

    private FragmentMenuBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.cartFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), CartActivity.class));
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) requireActivity()).getComponent().inject(this);
        requireActivity().setTitle(R.string.label_menu);

        MenuFragmentViewModel viewModel = ViewModelProviders.of(this, vmFactory).get(MenuFragmentViewModel.class);
        viewModel.categoriesLiveData.observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(@Nullable List<Category> data) {
                updateData(data);
            }
        });
        viewModel.stateLiveData.observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                updateState(state);
            }
        });
    }

    private void updateData(@Nullable List<Category> data) {
        if (data != null) {
            MenuFragmentPagerAdapter adapter = new MenuFragmentPagerAdapter(getFragmentManager(), data);
            binding.viewPager.setAdapter(adapter);
            binding.tabLayout.setupWithViewPager(binding.viewPager);
        }
    }

    private void updateState(@Nullable State state) {
        if (state != null) {
            binding.setState(state);
        }
    }

    public static MenuFragment newInstance() {
        Bundle args = new Bundle();
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }
}