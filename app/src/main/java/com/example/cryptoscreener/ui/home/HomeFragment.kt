package com.example.cryptoscreener.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoscreener.databinding.FragmentHomeBinding
import com.example.cryptoscreener.ui.screener.CryptoAdapter
import com.example.cryptoscreener.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var gainersAdapter: CryptoAdapter
    private lateinit var losersAdapter: CryptoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeViewModel()
        viewModel.loadData()
    }

    private fun setupRecyclerViews() {
        gainersAdapter = CryptoAdapter(mutableListOf())
        binding.rvGainers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGainers.adapter = gainersAdapter

        losersAdapter = CryptoAdapter(mutableListOf())
        binding.rvLosers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLosers.adapter = losersAdapter
    }

    private fun observeViewModel() {
        viewModel.topGainers.observe(viewLifecycleOwner) { cryptos ->
            gainersAdapter.updateData(cryptos)
        }

        viewModel.topLosers.observe(viewLifecycleOwner) { cryptos ->
            losersAdapter.updateData(cryptos)
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