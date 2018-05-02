package com.timotiusoktorio.pencake.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.timotiusoktorio.pencake.R;
import com.timotiusoktorio.pencake.data.model.CartItem;
import com.timotiusoktorio.pencake.data.model.Product;
import com.timotiusoktorio.pencake.databinding.ListItemCartBinding;

import java.util.List;

public class CartRvAdapter extends RecyclerView.Adapter<CartRvAdapter.ViewHolder> {

    private List<CartItem> cart;
    private OnItemClickListener listener;

    public CartRvAdapter(List<CartItem> cart) {
        this.cart = cart;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<CartItem> data) {
        cart.clear();
        cart.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemCartBinding binding = ListItemCartBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(cart.get(position));
    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ListItemCartBinding binding;

        ViewHolder(ListItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final CartItem cartItem) {
            Product product = Product.fromJson(cartItem.getProductJson());
            binding.setCartItem(cartItem);
            binding.setProduct(product);

            Picasso.get().load(product.getImageUrls().get(0)).fit().centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .into(binding.productPhotoIv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(cartItem);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {

        void onItemClick(CartItem cartItem);
    }
}