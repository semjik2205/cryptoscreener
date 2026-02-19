package com.example.cryptoscreener.ui.screener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoscreener.databinding.FragmentScreenerBinding
import com.example.cryptoscreener.model.Crypto

class ScreenerFragment : Fragment() {

    private var _binding: FragmentScreenerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScreenerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val placeholderData = listOf(
            Crypto(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                currentPrice = 62400.00,
                priceChangePercent = 2.45,
                imageUrl = ""
            ),
            Crypto(
                id = "ethereum",
                name = "Ethereum",
                symbol = "ETH",
                currentPrice = 3180.50,
                priceChangePercent = -1.12,
                imageUrl = ""
            )
        )

        binding.rvCryptoList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCryptoList.adapter = CryptoAdapter(placeholderData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}