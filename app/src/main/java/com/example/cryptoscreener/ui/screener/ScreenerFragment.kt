package com.example.cryptoscreener.ui.screener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoscreener.databinding.FragmentScreenerBinding
import com.example.cryptoscreener.viewmodel.ScreenerViewModel

class ScreenerFragment : Fragment() {

    private var _binding: FragmentScreenerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScreenerViewModel by viewModels()
    private lateinit var adapter: CryptoAdapter

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
        observeViewModel()
        viewModel.loadCryptos()
    }

    private fun setupRecyclerView() {
        adapter = CryptoAdapter(mutableListOf())
        binding.rvCryptoList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCryptoList.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.cryptoList.observe(viewLifecycleOwner) { cryptos ->
            adapter.updateData(cryptos)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = error
            } else {
                binding.tvError.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}