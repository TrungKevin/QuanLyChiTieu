package com.trungkien.qlychitieu2

import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.trungkien.qlychitieu2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Lấy dữ liệu từ Intent
        val username = intent.getStringExtra("username")
        if (username != null) {
            showToast("Xin chào, $username!")
        }

        // Lấy NavController từ NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Truyền dữ liệu sang FourthFragment
        val bundle = Bundle()                        //*
        bundle.putString("username", username)        //*
        navController.setGraph(navController.graph, bundle)

//        // Thiết lập Toolbar làm ActionBar
//        val toolbarr = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbarr) // Gọi trước setupActionBarWithNavController

        // Thiết lập bottom navigation với NavController
        binding.bottomBar.setupWithNavController(navController)
    }

    // Thiết lập mục được chọn trong bottom navigation
    fun setSelectedItem(pos: Int) {
        binding.bottomBar.setSelectedItem(pos)
    }

    // Thiết lập badge trên bottom navigation
    fun setBadge(pos: Int) {
        binding.bottomBar.setBadge(pos)
    }

    // Xóa badge khỏi bottom navigation
    fun removeBadge(pos: Int) {
        binding.bottomBar.removeBadge(pos)
    }

    // Hiển thị Toast
    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}