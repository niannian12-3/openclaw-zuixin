package com.oa.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.oa.app.util.AuthManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var authManager: AuthManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        authManager = AuthManager(this)
        
        // 检查登录状态
        lifecycleScope.launch {
            if (!authManager.isLoggedIn()) {
                // 未登录，导航到登录页面
                val navController = findNavController(R.id.nav_host_fragment)
                navController.navigate(R.id.loginFragment)
            }
        }
        
        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)
    }
    
    suspend fun isLoggedIn(): Boolean {
        return authManager.isLoggedIn()
    }
    
    suspend fun logout() {
        authManager.clearAuth()
    }
}
