package com.timotiusoktorio.pencake.di;

import com.timotiusoktorio.pencake.ui.MainActivity;
import com.timotiusoktorio.pencake.ui.addtocart.AddToCartActivity;
import com.timotiusoktorio.pencake.ui.cart.CartActivity;
import com.timotiusoktorio.pencake.ui.favorites.FavoritesFragment;
import com.timotiusoktorio.pencake.ui.menu.MenuFragment;
import com.timotiusoktorio.pencake.ui.menu.MenuListFragment;
import com.timotiusoktorio.pencake.ui.orderdetail.OrderDetailActivity;
import com.timotiusoktorio.pencake.ui.orders.OrdersListFragment;
import com.timotiusoktorio.pencake.ui.productdetail.ProductDetailActivity;
import com.timotiusoktorio.pencake.ui.profile.ProfileFragment;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity target);

    void inject(MenuFragment target);

    void inject(MenuListFragment target);

    void inject(ProductDetailActivity target);

    void inject(AddToCartActivity target);

    void inject(CartActivity target);

    void inject(OrdersListFragment target);

    void inject(OrderDetailActivity target);

    void inject(FavoritesFragment target);

    void inject(ProfileFragment target);
}