package com.example.stajtvplus.ui.update


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.stajtvplus.Login
import com.example.stajtvplus.R
import com.example.stajtvplus.configs.ApiClient
import com.example.stajtvplus.databinding.FragmentProfileUpdateBinding
import com.example.stajtvplus.models.Address
import com.example.stajtvplus.models.JWTData
import com.example.stajtvplus.services.IDummyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileUpdateFragment : Fragment() {

    private var _binding: FragmentProfileUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var dummyService: IDummyService
    private var existingUserInfo: JWTData? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileUpdateBinding.inflate(inflater, container, false)
        dummyService = ApiClient.getClient().create(IDummyService::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Güncelleme düğmesine tıklayınca güncelleme işlemlerini gerçekleştir
        binding.btnUpdate.setOnClickListener {
            updateUserInfo()
            val navController = findNavController()
            navController.navigate(R.id.navigation_profile)
        }
        getUserInfo()
    }

    private fun getUserInfo() {
        val userId = Login.DataManager.userId ?: return

        val service = ApiClient.getClient().create(IDummyService::class.java)
        val call = service.getUserInfoById(userId)

        call.enqueue(object : Callback<JWTData> {
            override fun onResponse(call: Call<JWTData>, response: Response<JWTData>) {
                if (response.isSuccessful) {
                    val userInfo = response.body()
                    if (userInfo != null) {
                        existingUserInfo = userInfo // Mevcut bilgileri sakla

                        // Kullanıcı bilgilerini giriş alanlarına yerleştir
                        binding.etMail.setText(userInfo.email)
                        binding.etPhone.setText(userInfo.phone)
                        binding.etCity.setText(userInfo.address.city)
                        binding.etAdress.setText(userInfo.address.address)
                        Glide.with(requireContext())
                            .load(userInfo.image)
                            .into(binding.imgUser)
                    }
                } else {
                    Log.e("ProfileFragment", "API call failed with code: ${response.code()}")
                    Log.e("ProfileFragment", "Error message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JWTData>, t: Throwable) {
                Log.e("ProfileFragment", "API call failed", t)
            }
        })
    }

    private fun updateUserInfo() {

        // Eski kullanıcı bilgilerini al
        val userInfo = existingUserInfo ?: return // Eğer nullsa güncelleme yapma
        Log.d("ProfileUpdateFragment", "userInfo: $userInfo")
        // Yeni bilgileri al
        val newEmail = binding.etMail.text.toString()
        val newPhone = binding.etPhone.text.toString()
        val newCity = binding.etCity.text.toString()
        val newAddress = binding.etAdress.text.toString()

        // Yalnızca değiştirilmek istenen bilgileri içeren yeni bir kopya oluşturun
        val updatedUserInfo = userInfo.copy(

            email = newEmail,
            phone = newPhone,
            address = Address(city = newCity, address = newAddress),
            lastName = "deneme"
        )
        Log.d("ProfileUpdateFragment", "updatedUserInfo: $updatedUserInfo")
        Log.d("ProfileUpdateFragment", "Updating user info...")
        val userIdString: String = Login.DataManager.userId ?: return

        val call = dummyService.updateUserInfo(userIdString, updatedUserInfo)


        call.enqueue(object : Callback<JWTData> {
            override fun onResponse(call: Call<JWTData>, response: Response<JWTData>) {
                if (response.isSuccessful) {
                    Log.d("ProfileUpdateFragment", "API call successful")
                    // Kullanıcı bilgileri başarıyla güncellendi, gerekirse geri dönüş değerini kullanabilirsiniz.
                    // Örnek olarak geri dönüş değeri JWTData nesnesi olabilir.
                    Toast.makeText(requireContext(), "Bilgileriniz güncellendi.", Toast.LENGTH_SHORT).show()
                    existingUserInfo = updatedUserInfo


                    // Geri tuşuna basıldığında ProfileFragment'e geri dön findNavController().popBackStack()
                } else {
                    Log.e("ProfileUpdateFragment", "API call failed with code: ${response.code()}")
                    Log.e("ProfileUpdateFragment", "Error message: ${response.message()}")
                    // API'den başarısız cevap geldiyse hata mesajını kullanıcıya gösterebilirsiniz.
                    Toast.makeText(requireContext(), "Bilgiler güncellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JWTData>, t: Throwable) {
                // API çağrısı başarısız olduysa hata mesajı gösterebilirsiniz
                Log.e("ProfileUpdateFragment", "API call failed", t)
                Toast.makeText(requireContext(), "Bilgiler güncellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}