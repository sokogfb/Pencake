package com.timotiusoktorio.pencake.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.timotiusoktorio.pencake.R
import com.timotiusoktorio.pencake.extensions.inflate
import kotlinx.android.synthetic.main.list_item_size.view.*

class SizeRvAdapter(private val sizes: List<String>,
                    private var lastSelectedPosition: Int,
                    private val listener: (Int) -> Unit) : RecyclerView.Adapter<SizeRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.list_item_size))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(sizes[position])

    override fun getItemCount(): Int = sizes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(size: String) = with(itemView) {
            sizeTv.text = size
            sizeRadioBtn.isChecked = (lastSelectedPosition == adapterPosition)
            sizeRadioBtn.setOnClickListener { selectSize() }
            setOnClickListener { selectSize() }
        }

        private fun selectSize() {
            if (lastSelectedPosition != adapterPosition) {
                lastSelectedPosition = adapterPosition
                listener(lastSelectedPosition)
                notifyDataSetChanged()
            }
        }
    }
}