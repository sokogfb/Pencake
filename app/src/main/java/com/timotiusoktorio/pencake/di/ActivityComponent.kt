package com.timotiusoktorio.pencake.di

import com.timotiusoktorio.pencake.ui.MainActivity
import com.timotiusoktorio.pencake.ui.addtocart.AddToCartActivity
import com.timotiusoktorio.pencake.ui.cart.CartActivity
import com.timotiusoktorio.pencake.ui.favorites.FavoritesFragment
import com.timotiusoktorio.pencake.ui.menu.MenuFragment
import com.timotiusoktorio.pencake.ui.menu.MenuListFragment
import com.timotiusoktorio.pencake.ui.orderdetail.OrderDetailActivity
import com.timotiusoktorio.pencake.ui.orders.OrdersListFragment
import com.timotiusoktorio.pencake.ui.productdetail.ProductDetailActivity
import com.timotiusoktorio.pencake.ui.profile.ProfileFragment

import dagger.Component

@ActivityScope
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class)])
interface ActivityComponent {

    fun inject(target: MainActivity)

    fun inject(target: MenuFragment)

    fun inject(target: MenuListFragment)

    fun inject(target: ProductDetailActivity)

    fun inject(target: AddToCartActivity)

    fun inject(target: CartActivity)

    fun inject(target: OrdersListFragment)

    fun inject(target: OrderDetailActivity)

    fun inject(target: FavoritesFragment)

    fun inject(target: ProfileFragment)
}