package com.trungkien.qlychitieu2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var accountEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView
    private lateinit var appRepository: AppRepository
    private lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_user)

        // Khởi tạo Repository
        val database = AppDatabase.getDatabase(this)
        appRepository = AppRepository(database.accountDAO(), database.chiTietThuChiDAO())

        // Ánh xạ các view
        accountEditText = findViewById(R.id.edt_account)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.btnLogin)
        registerTextView = findViewById(R.id.btn_CR_Register)

        // Khởi tạo SharedPreferences
        sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        loginButton.setOnClickListener {
            val account = accountEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (account.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tài khoản và mật khẩu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kiểm tra đăng nhập bằng cách sử dụng Repository
            lifecycleScope.launch {
                val account = appRepository.login(account, password)
                if (account != null) {

                    // Lưu username vào SharedPreferences
                    sharedPref.edit().putString("USERNAME", account.username).apply()

                    // Đăng nhập thành công, chuyển sang MainActivity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("username", account.username)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) // Hiệu ứng chuyển cảnh
                    finish() // Đóng LoginActivity để người dùng không thể quay lại
                } else {
                    // Đăng nhập thất bại
                    Toast.makeText(this@LoginActivity, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                }
            }

        }


        // Xử lý sự kiện click cho Register TextView
        registerTextView.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out) // Hiệu ứng chuyển cảnh
        }



    }
}