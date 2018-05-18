package com.timotiusoktorio.pencake.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.data.model.Order
import com.timotiusoktorio.pencake.extensions.inflate
import com.timotiusoktorio.pencake.extensions.toShortDateFormat
import kotlinx.android.synthetic.main.list_item_order.view.*
import java.util.*

class OrdersRvAdapter(private val orders: MutableList<Order> = mutableListOf(),
                      private val listener: (Order) -> Unit) : RecyclerView.Adapter<OrdersRvAdapter.ViewHolder>() {

    fun updateData(data: List<Order>) {
        orders.clear()
        orders.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.list_item_order))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position])

    override fun getItemCount(): Int = orders.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(order: Order) = with(itemView) {
            orderDateTv.text = Date(order.orderDate).toShortDateFormat()
            totalOrderedTv.text = context.resources.getQuantityString(R.plurals.total_items_ordered, order.cart.size, order.cart.size)
            orderSubtotalTv.text = context.getString(R.string.string_format_price, order.subtotal)
            setOnClickListener { listener(order) }
        }
    }
}