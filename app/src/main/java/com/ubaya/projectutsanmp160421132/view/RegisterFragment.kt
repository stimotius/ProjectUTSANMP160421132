package com.ubaya.projectutsanmp160421132.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubaya.projectutsanmp160421132.R
import com.ubaya.projectutsanmp160421132.databinding.FragmentRegisterBinding
import org.json.JSONObject

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private var queue: RequestQueue? = null
    val TAG = "volleyTag"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
//        return inflater.inflate(R.layout.fragment_register, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val nama_depan = binding.txtNamaDepan.text.toString()
            val nama_belakang = binding.txtNamaBelakang.text.toString()
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassw.text.toString()
            val konfPassw = binding.txtKonfirmPassword.text.toString()

            val dialog = AlertDialog.Builder(activity)
            if (password == konfPassw) {
                dialog.setTitle("Konfirmasi")
                dialog.setMessage("Apakah anda ingin melakukan registrasi?")
                dialog.setPositiveButton("Register", DialogInterface.OnClickListener { dialog, which ->
                    register(it, username, nama_depan, nama_belakang, email, password)
                })
                dialog.setNegativeButton("Batal", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                dialog.create().show()
            } else {
                dialog.setTitle("Informasi")
                dialog.setMessage("Gagal mendaftarkan user.\nCek apakah password dengan konfirmasinya sama")
                dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                dialog.create().show()
            }
        }
    }

    fun register(view: View, username: String, nama_depan: String, nama_belakang: String, email: String, password: String) {
        Log.d("register", "registerVolley")

        queue = Volley.newRequestQueue(context)
        val url = "http://10.0.2.2/anmp_utsproject/register.php"

        val alert = AlertDialog.Builder(activity)
        alert.setTitle("Informasi")

        val stringRequest = object: StringRequest(
            Request.Method.POST,
            url,
            {
                Log.d("cekbisa", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    alert.setMessage("Pendaftaran Berhasil.\nSilahkan Login")
                    alert.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        val action = RegisterFragmentDirections.actionLoginFragment()
                        Navigation.findNavController(view).navigate(action)
                    })
                } else {
                    alert.setMessage("Gagal mendaftarkan user.")
                    alert.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
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
                params["username"] = username
                params["nama_depan"] = nama_depan
                params["nama_belakang"] = nama_belakang
                params["email"] = email
                params["password"] = password
                return params
            }
        }

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}