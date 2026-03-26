package com.oa.app

object Config {
    const val PREF_TOKEN = "user_token"
    const val PREF_EMPLOYEE_ID = "employee_id"
    const val PREF_NAME = "user_name"
    
    // OpenClaw Gateway 服务器地址 - 更新为当前电脑的局域网 IP
    const val API_BASE_URL = "http://192.168.11.42:18789/"
    
    // 认证 Token (从 Gateway 配置获取) - 更新为正确的 Token
    const val GATEWAY_TOKEN = "3abf9fa16fdf569647fd0afea37f3836081293ee4c007138"
    
    // 网络超时设置 (秒)
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
}
