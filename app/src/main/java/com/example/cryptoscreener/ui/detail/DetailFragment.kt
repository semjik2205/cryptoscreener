package com.example.cryptoscreener.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cryptoscreener.databinding.FragmentDetailBinding
import com.example.cryptoscreener.viewmodel.DetailViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.example.cryptoscreener.R

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()

    private lateinit var coinId: String
    private lateinit var coinName: String
    private var coinPrice: Double = 0.0
    private var coinChange: Double = 0.0

    companion object {
        const val ARG_COIN_ID = "coin_id"
        const val ARG_COIN_NAME = "coin_name"
        const val ARG_COIN_PRICE = "coin_price"
        const val ARG_COIN_CHANGE = "coin_change"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем данные из Bundle
        coinId = arguments?.getString(ARG_COIN_ID) ?: ""
        coinName = arguments?.getString(ARG_COIN_NAME) ?: ""
        coinPrice = arguments?.getFloat(ARG_COIN_PRICE)?.toDouble() ?: 0.0
        coinChange = arguments?.getFloat(ARG_COIN_CHANGE)?.toDouble() ?: 0.0

        setupUI()
        setupChart()
        setupTimeframeButtons()
        observeViewModel()
        viewModel.loadChart(coinId)
    }

    private fun setupUI() {
        binding.tvCoinName.text = coinName
        binding.tvPrice.text = "$${String.format("%,.2f", coinPrice)}"
        val changeText = "${if (coinChange >= 0) "+" else ""}${
            String.format("%.2f", coinChange)
        }%"
        binding.tvChange.text = changeText
        binding.tvChange.setTextColor(
            if (coinChange >= 0) Color.parseColor("#3FB950")
            else Color.parseColor("#F85149")
        )
    }

    private fun setupChart() {
        binding.lineChart.apply {
            setBackgroundColor(Color.parseColor("#0D1117"))
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)

            // Убираем перекрестие
            marker = object : com.github.mikephil.charting.components.MarkerView(
                requireContext(), R.layout.marker_view
            ) {
                override fun refreshContent(e: Entry?, highlight: com.github.mikephil.charting.highlight.Highlight?) {
                    e?.let {
                        val price = "$${String.format("%,.2f", it.y.toDouble())}"
                        findViewById<android.widget.TextView>(R.id.tvMarkerPrice).text = price
                    }
                    super.refreshContent(e, highlight)
                }
            }

            xAxis.apply {
                setDrawGridLines(false)
                textColor = Color.parseColor("#8B949E")
                textSize = 10f
                position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
            }
            axisLeft.apply {
                textColor = Color.parseColor("#8B949E")
                setDrawGridLines(true)
                gridColor = Color.parseColor("#21262D")
            }
            axisRight.isEnabled = false
        }
    }

    private fun setupTimeframeButtons() {
        val buttons = listOf(
            binding.btn24h to "1",
            binding.btn7d to "7",
            binding.btn1m to "30",
            binding.btn3m to "90"
        )

        buttons.forEach { (button, days) ->
            button.setOnClickListener {
                buttons.forEach { (btn, _) ->
                    btn.backgroundTintList = android.content.res.ColorStateList
                        .valueOf(Color.parseColor("#161B22"))
                    btn.setTextColor(Color.WHITE)
                }
                button.backgroundTintList = android.content.res.ColorStateList
                    .valueOf(Color.parseColor("#F0B90B"))
                button.setTextColor(Color.BLACK)
                viewModel.loadChart(coinId, days)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.chartData.observe(viewLifecycleOwner) { data ->
            updateChart(data)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun updateChart(data: List<Pair<Long, Double>>) {
        val entries = data.mapIndexed { index, point ->
            Entry(index.toFloat(), point.second.toFloat())
        }

        // Форматируем даты для оси X
        val dates = data.map { point ->
            val sdf = java.text.SimpleDateFormat("dd/MM", java.util.Locale.getDefault())
            sdf.format(java.util.Date(point.first))
        }

        binding.lineChart.xAxis.valueFormatter = object :
            com.github.mikephil.charting.formatter.ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index >= 0 && index < dates.size) dates[index] else ""
            }
        }

        // Показываем только 5 меток на оси X
        binding.lineChart.xAxis.setLabelCount(5, true)

        val dataSet = LineDataSet(entries, "").apply {
            color = Color.parseColor("#F0B90B")
            setDrawCircles(false)
            setDrawValues(false)
            lineWidth = 2f
            setDrawFilled(true)
            fillColor = Color.parseColor("#F0B90B")
            fillAlpha = 30
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawHighlightIndicators(true)
            highLightColor = Color.parseColor("#F0B90B")
        }

        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}