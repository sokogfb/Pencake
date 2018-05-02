package com.timotiusoktorio.pencake.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.timotiusoktorio.pencake.R;
import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.databinding.ListItemProductBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductsRvAdapter extends RecyclerView.Adapter<ProductsRvAdapter.ViewHolder> {

    private final List<Product> products = new ArrayList<>();
    private final OnItemClickListener listener;

    public ProductsRvAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<Product> data) {
        products.clear();
        products.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemProductBinding binding = ListItemProductBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ListItemProductBinding binding;

        ViewHolder(ListItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Product product) {
            binding.setProduct(product);

            Picasso.get().load(product.getImageUrls().get(0)).fit().centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .into(binding.productPhotoIv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(product);
                }
            });
        }
    }

    public interface OnItemClickListener {

        void onItemClick(Product product);
    }
}