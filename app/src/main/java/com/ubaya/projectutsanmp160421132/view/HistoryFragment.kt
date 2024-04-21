package com.ubaya.projectutsanmp160421132.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ubaya.projectutsanmp160421132.R
import com.ubaya.projectutsanmp160421132.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_history, container, false)
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}