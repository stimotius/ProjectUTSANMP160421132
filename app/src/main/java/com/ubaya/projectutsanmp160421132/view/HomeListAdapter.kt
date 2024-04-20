package com.ubaya.projectutsanmp160421132.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.projectutsanmp160421132.databinding.CardItemBinding
import com.ubaya.projectutsanmp160421132.model.VideoEditor

class HomeListAdapter(val fmList: ArrayList<VideoEditor>): RecyclerView.Adapter<HomeListAdapter.HomeViewHolder>() {
    class HomeViewHolder(val binding: CardItemBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return fmList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        with (holder.binding) {
            HomeActivity.load_picture(holder.itemView, fmList[position].url_gambar.toString(), imgPhoto)
            txtNama.text = fmList[position].nama
            txtUsername.text = "By ${fmList[position].pembuat}"
            txtContent.text = fmList[position].deskripsi

            btnRead.setOnClickListener {
                val action = HomeFragmentDirections.actionDetailFragment(fmList[position].id.toString().toInt())
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    fun updateData(newList: ArrayList<VideoEditor>) {
        fmList.clear()
        fmList.addAll(newList)
        notifyDataSetChanged()
    }

}