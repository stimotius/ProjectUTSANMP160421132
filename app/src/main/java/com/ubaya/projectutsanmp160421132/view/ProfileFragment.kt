package com.ubaya.projectutsanmp160421132.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.projectutsanmp160421132.R
import com.ubaya.projectutsanmp160421132.databinding.FragmentProfileBinding
import com.ubaya.projectutsanmp160421132.model.Pengguna
import org.json.JSONObject

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var queue: RequestQueue? = null
    val TAG = "volleyTag"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false)
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var shared = activity?.packageName
        var sharedPref: SharedPreferences = requireActivity().getSharedPreferences(shared, Context.MODE_PRIVATE)
        var res = sharedPref.getString("KEY_USER", "")
        Log.d("cek", res.toString())

        if (res != null) {
            val sType = object: TypeToken<Pengguna>() { }.type
            val user = Gson().fromJson(res.toString(), sType) as Pengguna

            HomeActivity.load_picture(requireView(), "https://picsum.photos/300/200", binding.imageView3)

            binding.txtProfilUsername.text = "Anda login sebagai: ${user.nama_depan} ${user.nama_belakang}"

            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle("Konfirmasi")

            binding.btnUpdate.setOnClickListener {
                dialog.setMessage("Apakah anda yakin ingin mengganti data diri anda?")
                dialog.setPositiveButton("Ganti", DialogInterface.OnClickListener { dialog, which ->
                    val new_nama_depan = binding.txtUbahNamaDepan.text.toString()
                    val new_nama_belakang = binding.txtUbahNamaBelakang.text.toString()
                    val new_pass = binding.txtUbahPassword.text.toString()
                    updateData(user, new_nama_depan, new_nama_belakang, new_pass)
                })
                dialog.setNegativeButton("Batal", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                dialog.create().show()
            }

            binding.btnLogout.setOnClickListener {
                dialog.setMessage("Apakah anda yakin ingin melakukan logout?")
                dialog.setPositiveButton("Logout", DialogInterface.OnClickListener { dialog, which ->
                    HomeActivity.logout(requireActivity())
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                })
                dialog.setNegativeButton("Batal", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                dialog.create().show()
            }
        }
    }

    fun updateData(user: Pengguna, nama_depan: String, nama_belakang: String, password: String) {
        var new_nama_depan: String?
        var new_nama_belakang: String?
        var new_pass: String?

        if (nama_depan != "") {
            new_nama_depan = nama_depan
        } else {
            new_nama_depan = user.nama_depan
        }

        if (nama_belakang != "") {
            new_nama_belakang = nama_belakang
        } else {
            new_nama_belakang = user.nama_belakang
        }

        if (password != "") {
            new_pass = password
        } else {
            new_pass = user.password
        }

        Log.d("update", "updateVolley")

        queue = Volley.newRequestQueue(context)
        val url = "http://10.0.2.2/project_uts_anmp/update_data.php"

        val alert = AlertDialog.Builder(activity)
        alert.setTitle("Informasi")

        val stringRequest = object: StringRequest(
            Request.Method.POST,
            url,
            {
                Log.d("cekbisa", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    alert.setMessage("Berhasil melakukan update data user.")
                    alert.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                } else {
                    alert.setMessage("Gagal melakukan update data user.")
                    alert.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    alert.create().show()
                }
                alert.create().show()
            },
            {
                Log.e("cekerror", it.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["id"] = user.id.toString()
                params["nama_depan"] = new_nama_depan.toString()
                params["nama_belakang"] = new_nama_belakang.toString()
                params["password"] = new_pass.toString()
                return params
            }
        }

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}