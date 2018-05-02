package com.timotiusoktorio.pencake.ui.orders;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timotiusoktorio.pencake.data.model.Order;
import com.timotiusoktorio.pencake.data.model.State;
import com.timotiusoktorio.pencake.databinding.FragmentOrdersListBinding;
import com.timotiusoktorio.pencake.ui.BaseActivity;
import com.timotiusoktorio.pencake.ui.orderdetail.OrderDetailActivity;

import java.util.List;

import javax.inject.Inject;

public class OrdersListFragment extends Fragment {

    private static final String ARG_TAB_INDEX = "ARG_TAB_INDEX";

    @Inject OrdersListFragmentViewModelFactory vmFactory;

    private FragmentOrdersListBinding binding;
    private OrdersRvAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new OrdersRvAdapter(new OrdersRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                startActivity(OrderDetailActivity.newIntent(requireActivity(), order.toJson()));
            }
        });
        binding.ordersRv.setAdapter(adapter);
        binding.ordersRv.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.ordersRv.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        binding.ordersRv.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) requireActivity()).getComponent().inject(this);

        if (getArguments() == null) {
            throw new IllegalStateException("Tab index was not sent here as an argument");
        }

        OrdersListFragmentViewModel viewModel = ViewModelProviders.of(this, vmFactory).get(OrdersListFragmentViewModel.class);
        viewModel.stateLiveData.observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                updateState(state);
            }
        });
        viewModel.ordersLiveData.observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(@Nullable List<Order> data) {
                updateData(data);
            }
        });

        int tabIndex = getArguments().getInt(ARG_TAB_INDEX);
        viewModel.loadOrders(tabIndex);
    }

    private void updateState(@Nullable State state) {
        if (state != null) {
            binding.setState(state);
        }
    }

    private void updateData(@Nullable List<Order> data) {
        if (data != null) {
            adapter.updateData(data);
        }
    }

    public static OrdersListFragment newInstance(int tabIndex) {
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_INDEX, tabIndex);
        OrdersListFragment fragment = new OrdersListFragment();
        fragment.setArguments(args);
        return fragment;
    }
}