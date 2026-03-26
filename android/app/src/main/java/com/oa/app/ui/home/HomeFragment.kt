package com.oa.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.oa.app.Config
import com.oa.app.R
import com.oa.app.data.api.RetrofitClient
import com.oa.app.databinding.FragmentHomeBinding
import com.oa.app.util.AuthManager
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthManager
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        authManager = AuthManager(requireContext())
        
        // 设置欢迎信息
        lifecycleScope.launch {
            val employeeId = getEmployeeId()
            binding.tvWelcome.text = "欢迎, $employeeId"
            
            // 测试 Gateway 连接
            testGatewayConnection()
        }
        
        // 设置按钮点击事件
        binding.btnTestConnection.setOnClickListener {
            testGatewayConnection()
        }
        
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }
    
    private suspend fun getEmployeeId(): String {
        // 从 SharedPreferences 获取 employeeId
        return authManager.getToken()?.let { token ->
            "OpenClaw User"
        } ?: "未登录用户"
    }
    
    private fun testGatewayConnection() {
        lifecycleScope.launch {
            try {
                binding.tvConnectionStatus.text = "正在测试连接..."
                
                val response = RetrofitClient.apiService.getGatewayStatus("Bearer ${Config.GATEWAY_TOKEN}")
                
                if (response.isSuccessful) {
                    binding.tvConnectionStatus.text = "✓ Gateway 连接正常"
                    binding.tvConnectionStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
                } else {
                    binding.tvConnectionStatus.text = "✗ Gateway 连接失败: ${response.code()}"
                    binding.tvConnectionStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
                }
            } catch (e: Exception) {
                binding.tvConnectionStatus.text = "✗ 连接错误: ${e.message}"
                binding.tvConnectionStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
            }
        }
    }
    
    private fun logout() {
        lifecycleScope.launch {
            authManager.clearAuth()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}