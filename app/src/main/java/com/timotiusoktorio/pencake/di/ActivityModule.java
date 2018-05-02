package com.timotiusoktorio.pencake.di;

import com.timotiusoktorio.pencake.data.source.DataManager;
import com.timotiusoktorio.pencake.ui.BaseActivity;
import com.timotiusoktorio.pencake.ui.addtocart.AddToCartViewModelFactory;
import com.timotiusoktorio.pencake.ui.cart.CartViewModelFactory;
import com.timotiusoktorio.pencake.ui.favorites.FavoritesViewModelFactory;
import com.timotiusoktorio.pencake.ui.menu.MenuFragmentViewModelFactory;
import com.timotiusoktorio.pencake.ui.menu.MenuListFragmentViewModelFactory;
import com.timotiusoktorio.pencake.ui.orderdetail.OrderDetailViewModelFactory;
import com.timotiusoktorio.pencake.ui.orders.OrdersListFragmentViewModelFactory;
import com.timotiusoktorio.pencake.ui.productdetail.ProductDetailViewModelFactory;
import com.timotiusoktorio.pencake.ui.profile.ProfileViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    public MenuFragmentViewModelFactory provideMenuFragmentViewModelFactory(DataManager dataManager) {
        return new MenuFragmentViewModelFactory(dataManager);
    }

    @Provides
    public MenuListFragmentViewModelFactory provideMenuListFragmentViewModelFactory(DataManager dataManager) {
        return new MenuListFragmentViewModelFactory(dataManager);
    }

    @Provides
    public ProductDetailViewModelFactory provideProductDetailViewModelFactory(DataManager dataManager) {
        return new ProductDetailViewModelFactory(dataManager);
    }

    @Provides
    public AddToCartViewModelFactory provideAddToCartViewModelFactory(DataManager dataManager) {
        return new AddToCartViewModelFactory(dataManager);
    }

    @Provides
    public CartViewModelFactory provideCartViewModelFactory(DataManager dataManager) {
        return new CartViewModelFactory(dataManager);
    }

    @Provides
    public OrdersListFragmentViewModelFactory provideOrdersListFragmentViewModelFactory(DataManager dataManager) {
        return new OrdersListFragmentViewModelFactory(dataManager);
    }

    @Provides
    public OrderDetailViewModelFactory provideOrderDetailActivityViewModelFactory(DataManager dataManager) {
        return new OrderDetailViewModelFactory(dataManager);
    }

    @Provides
    public FavoritesViewModelFactory provideFavoritesViewModelFactory(DataManager dataManager) {
        return new FavoritesViewModelFactory(dataManager);
    }

    @Provides
    public ProfileViewModelFactory provideProfileViewModelFactory(DataManager dataManager) {
        return new ProfileViewModelFactory(dataManager);
    }
}