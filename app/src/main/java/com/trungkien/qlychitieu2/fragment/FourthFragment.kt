package com.trungkien.qlychitieu2.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.trungkien.qlychitieu2.LoginActivity
import com.trungkien.qlychitieu2.SharedViewModel
import com.trungkien.qlychitieu2.databinding.FragmentFourthBinding

class FourthFragment : Fragment() {

    private lateinit var binding: FragmentFourthBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var sharedPref: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFourthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Test commit
        // Khởi tạo SharedPreferences
        sharedPref = requireActivity().getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE)

        // Khởi tạo SharedViewModel
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Lấy thông tin tài khoản từ SharedPreferences
        val username = sharedPref.getString("USERNAME", "")

        // Hiển thị tài khoản lên edt_taikhoan_frag4
        binding.edtTaikhoanFrag4.setText(username)


        // Xử lý sự kiện đăng xuất
        binding.btnLogoutFrag4.setOnClickListener {
            logout()
        }



        // Quan sát số dư từ SharedViewModel và cập nhật lên edt_sodu_frag4
        sharedViewModel.soDu.observe(viewLifecycleOwner, Observer { soDu ->
            soDu?.let {
                val formattedSoDu = if (soDu >= 0) "+${formatCurrency(soDu)}" else "${formatCurrency(soDu)}"
                binding.edtSoduFrag4.setText(formattedSoDu)
                binding.edtSoduFrag4.setTextColor(
                    if (soDu >= 0) requireContext().getColor(android.R.color.holo_green_dark)
                    else requireContext().getColor(android.R.color.holo_red_dark)
                )
            }
        })
    }

    private fun formatCurrency(amount: Double): String {
        val formatter = java.text.DecimalFormat("#,###")
        return formatter.format(amount)
    }

    private fun logout() {
        // Xóa thông tin đăng nhập từ SharedPreferences
        sharedPref.edit().clear().apply()

        // Chuyển hướng về LoginActivity
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)

        // Kết thúc MainActivity hoặc Activity hiện tại
        requireActivity().finish()
    }
}