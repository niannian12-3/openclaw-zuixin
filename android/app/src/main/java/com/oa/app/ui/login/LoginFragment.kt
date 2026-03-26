package com.oa.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.oa.app.Config
import com.oa.app.R
import com.oa.app.data.api.RetrofitClient
import com.oa.app.databinding.FragmentLoginBinding
import com.oa.app.util.AuthManager
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LoginFragment : Fragment() {
    
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authManager: AuthManager
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        authManager = AuthManager(requireContext())
        
        // 设置登录按钮点击事件
        binding.btnLogin.setOnClickListener {
            val employeeId = binding.etEmployeeId.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            if (employeeId.isEmpty() || password.isEmpty()) {
                showError("请输入工号和密码")
                return@setOnClickListener
            }
            
            // 使用 OpenClaw Gateway 认证方式
            loginWithGateway(employeeId, password)
        }
        
        // 检查是否已登录
        lifecycleScope.launch {
            if (authManager.isLoggedIn()) {
                // 已登录，跳转到主页面
                navigateToHome()
            }
        }
    }
    
    private fun loginWithGateway(employeeId: String, password: String) {
        binding.btnLogin.isEnabled = false
        binding.tvError.visibility = View.GONE
        
        lifecycleScope.launch {
            try {
                // OpenClaw Gateway 使用 Bearer Token 认证
                // 我们直接使用配置中的 Token
                val gatewayToken = Config.GATEWAY_TOKEN
                
                // 测试 Gateway 连接
                val response = RetrofitClient.apiService.getGatewayStatus("Bearer $gatewayToken")
                
                if (response.isSuccessful) {
                    // Gateway 连接成功，保存认证信息
                    authManager.saveToken(gatewayToken)
                    authManager.saveUserInfo(employeeId, "OpenClaw User")
                    
                    // 登录成功
                    Toast.makeText(requireContext(), "登录成功", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    // Gateway 认证失败
                    showError("Gateway 认证失败: ${response.code()}")
                }
            } catch (e: Exception) {
                showError("连接失败: ${e.message}")
            } finally {
                binding.btnLogin.isEnabled = true
            }
        }
    }
    
    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.visibility = View.VISIBLE
    }
    
    private fun navigateToHome() {
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}