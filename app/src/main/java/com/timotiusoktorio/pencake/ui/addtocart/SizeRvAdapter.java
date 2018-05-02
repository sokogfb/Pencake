package com.timotiusoktorio.pencake.ui.addtocart;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timotiusoktorio.pencake.databinding.ListItemSizeBinding;

import java.util.List;

class SizeRvAdapter extends RecyclerView.Adapter<SizeRvAdapter.ViewHolder> {

    private List<String> sizes;
    private int lastSelectedPosition;
    private OnSizeSelectedListener listener;

    SizeRvAdapter(List<String> sizes, int lastSelectedPosition, OnSizeSelectedListener listener) {
        this.sizes = sizes;
        this.lastSelectedPosition = lastSelectedPosition;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemSizeBinding binding = ListItemSizeBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(sizes.get(position));
    }

    @Override
    public int getItemCount() {
        return sizes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ListItemSizeBinding binding;

        ViewHolder(ListItemSizeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String size) {
            binding.setSize(size);
            binding.setChecked(lastSelectedPosition == getAdapterPosition());
            binding.sizeRadioBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectSize();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectSize();
                }
            });
        }

        private void selectSize() {
            if (lastSelectedPosition != getAdapterPosition()) {
                lastSelectedPosition = getAdapterPosition();
                listener.onSizeSelected(lastSelectedPosition);
                notifyDataSetChanged();
            }
        }
    }

    interface OnSizeSelectedListener {

        void onSizeSelected(int sizePosition);
    }
}