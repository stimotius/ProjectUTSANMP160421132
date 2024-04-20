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
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubaya.projectutsanmp160421132.R
import com.ubaya.projectutsanmp160421132.databinding.FragmentLoginBinding
import com.ubaya.projectutsanmp160421132.model.Pengguna
import org.json.JSONObject

class LoginFragment : Fragment() {
    private var queue: RequestQueue? = null
    val TAG = "volleyTag"
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
//        return inflater.inflate(R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPassw.text.toString()

            login(username, password)
        }

        binding.btnRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionRegisterFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    fun login(username: String, password: String) {
        Log.d("login", "loginVolley")

        queue = Volley.newRequestQueue(activity)
        val url = "http://10.0.2.2/anmp_utsproject/login.php"

        val alert = AlertDialog.Builder(activity)
        alert.setTitle("Informasi")

        val stringRequest = object : StringRequest(
            Request.Method.POST,
            url,
            {
                Log.d("cekdata", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    if (data.length() > 0) {
                        val datauser = data.getJSONObject(0)
                        val sType = object: TypeToken<Pengguna>() { }.type
                        val user = Gson().fromJson(datauser.toString(), sType) as Pengguna
                        Log.d("cekdata", user.toString())
                        alert.setMessage("Login Berhasil\nSelamat datang ${user.nama_depan} ${user.nama_belakang}")
                        alert.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                            val sharedPref = activity?.packageName
                            val shared: SharedPreferences = requireActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
                            val editor = shared.edit()
                            editor.putString("KEY_USER", datauser.toString())
                            editor.apply()

                            val intent = Intent(activity, HomeActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        })
                    }
                } else {
                    alert.setMessage("Login Gagal")
                    alert.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    })
                }
                alert.create().show()
            },
            {
                Log.e("error", it.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}