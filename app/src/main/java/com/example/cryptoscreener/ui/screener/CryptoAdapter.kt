package com.example.cryptoscreener.ui.screener

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoscreener.databinding.ItemCryptoBinding
import com.example.cryptoscreener.model.Crypto

class CryptoAdapter(private val items: MutableList<Crypto>) :
    RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    inner class CryptoViewHolder(private val binding: ItemCryptoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(crypto: Crypto) {
            binding.tvSymbol.text = crypto.symbol.uppercase()
            binding.tvName.text = crypto.name
            binding.tvPrice.text = "$${String.format("%,.2f", crypto.currentPrice)}"

            val changeText = "${if (crypto.priceChangePercent >= 0) "+" else ""}${
                String.format("%.2f", crypto.priceChangePercent)
            }%"
            binding.tvChange.text = changeText
            binding.tvChange.setTextColor(
                if (crypto.priceChangePercent >= 0) 0xFF3FB950.toInt()
                else 0xFFF85149.toInt()
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = ItemCryptoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CryptoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
    fun updateData(newItems: List<Crypto>) {
        (items as MutableList).clear()
        (items as MutableList).addAll(newItems)
        notifyDataSetChanged()
    }
}