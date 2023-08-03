package com.example.stajtvplus.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.stajtvplus.Login
import com.example.stajtvplus.MainActivity
import com.example.stajtvplus.R
import com.example.stajtvplus.configs.ApiClient
import com.example.stajtvplus.databinding.FragmentNotificationsBinding
import com.example.stajtvplus.databinding.FragmentProfileBinding
import com.example.stajtvplus.models.JWTData
import com.example.stajtvplus.services.IDummyService
import com.example.stajtvplus.ui.notifications.NotificationsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var dummyService: IDummyService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        getUserInfo()
        binding.btnUpdate.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.navigation_update)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    private fun getUserInfo() {
        val userId = Login.DataManager.userId ?: return

        val service = ApiClient.getClient().create(IDummyService::class.java)
        val call = service.getUserInfoById(userId)

        call.enqueue(object : Callback<JWTData> {
            override fun onResponse(call: Call<JWTData>, response: Response<JWTData>) {
                if (response.isSuccessful) {
                    val userInfo = response.body() // Kullanıcı bilgilerini alın
                    if (userInfo != null) {
                        // Kullanıcı bilgilerini TextView'lere yerleştirin

                        binding.txtUserName.text = userInfo.username
                        binding.txtFirstName.text="Name: ${userInfo.firstName}"
                        binding.txtLastName.text="Lastname: ${userInfo.lastName}"
                        binding.txtEmail.text = "Email: ${userInfo.email}"
                        binding.txtPhone.text = "Phone: ${userInfo.phone}"
                        binding.txtBirthDate.text="Birth Date: ${userInfo.birthDate}"
                        binding.txtAge.text="Age: ${userInfo.age}"
                        binding.txtCity.text="City: ${userInfo.address.city}"
                        binding.txtAdress.text="Adress: ${userInfo.address.address}"
                        Glide.with(requireContext())
                            .load(userInfo.image)
                            .into(binding.imgUser)
                    }
                }
            }

            override fun onFailure(call: Call<JWTData>, t: Throwable) {
                Log.e("ProfileFragment", "API call failed", t)
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}