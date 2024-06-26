package com.ubaya.projectutsanmp160421132.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ubaya.projectutsanmp160421132.R
import com.ubaya.projectutsanmp160421132.databinding.FragmentDetailBinding
import com.ubaya.projectutsanmp160421132.viewmodel.DetailViewModel

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_detail, container, false)
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        if (arguments != null) {
            val id = DetailFragmentArgs.fromBundle(requireArguments()).id
            viewModel.fetch(id)
        }

        observeViewModel()
    }

    fun checkPageNow(page: Int, size_par: Int) {
        with (binding) {
            when (page) {
                0 -> {
                    btnPrev.isEnabled = false
                    btnNext.isEnabled = true
                }
                size_par.minus(1) -> {
                    btnPrev.isEnabled = true
                    btnNext.isEnabled = false
                }
                else -> {
                    btnPrev.isEnabled = true
                    btnNext.isEnabled = true
                }
            }
        }
    }

    fun observeViewModel() {
        viewModel.dataLD.observe(viewLifecycleOwner, Observer {
            with (binding) {
                HomeActivity.load_picture(requireView(), it.url_gambar.toString(), imgPhotoDetail)
                var page = 0
                txtNama.text = it.nama
                txtUsername.text = "By ${it.pembuat}"
                val content_per_par = it.isi?.split("\n")
                Log.d("cekdata", content_per_par.toString())
                val size_par = content_per_par?.size

                txtContent.text = content_per_par?.get(page)
                checkPageNow(page, size_par!!.toInt())

                btnNext.setOnClickListener {
                    page++
                    txtContent.text = content_per_par[page]
                    checkPageNow(page, size_par.toInt())
                }

                btnPrev.setOnClickListener {
                    page--
                    txtContent.text = content_per_par[page]
                    checkPageNow(page, size_par.toInt())
                }
            }
        })
    }
}