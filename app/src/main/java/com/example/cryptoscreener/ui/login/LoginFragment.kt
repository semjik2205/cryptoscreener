package com.example.cryptoscreener.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cryptoscreener.R
import com.example.cryptoscreener.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val enteredPin = StringBuilder()
    private val dots by lazy {
        listOf(binding.dot1, binding.dot2, binding.dot3, binding.dot4)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNumpad()
    }

    private fun setupNumpad() {
        val numberButtons = listOf(
            binding.btn0 to "0", binding.btn1 to "1", binding.btn2 to "2",
            binding.btn3 to "3", binding.btn4 to "4", binding.btn5 to "5",
            binding.btn6 to "6", binding.btn7 to "7", binding.btn8 to "8",
            binding.btn9 to "9"
        )

        numberButtons.forEach { (button, digit) ->
            button.setOnClickListener { onDigitPressed(digit) }
        }

        binding.btnDelete.setOnClickListener { onDeletePressed() }
    }

    private fun onDigitPressed(digit: String) {
        if (enteredPin.length >= 4) return
        enteredPin.append(digit)
        updateDots()
        if (enteredPin.length == 4) onPinComplete()
    }

    private fun onDeletePressed() {
        if (enteredPin.isEmpty()) return
        enteredPin.deleteCharAt(enteredPin.length - 1)
        updateDots()
    }

    private fun updateDots() {
        dots.forEachIndexed { index, dot ->
            dot.setBackgroundResource(
                if (index < enteredPin.length) R.drawable.pin_dot_filled
                else R.drawable.pin_dot_empty
            )
        }
    }

    private fun onPinComplete() {
        // PIN логика будет добавлена в Шаге 3
        // Пока любые 4 цифры открывают скринер
        findNavController().navigate(R.id.action_login_to_screener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}