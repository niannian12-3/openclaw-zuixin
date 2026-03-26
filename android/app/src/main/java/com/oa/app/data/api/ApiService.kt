package com.oa.app.data.api

import com.oa.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // OpenClaw Gateway 认证
    // 修改点：Token 从 Header 移到了 URL 参数 (?token=xxx)
    @GET("api/status")
    suspend fun getGatewayStatus(@Query("token") token: String): Response<GatewayStatusResponse>
    
    // ❌ 已删除：login 接口 (OpenClaw 不需要单独登录)
    
    // 兼容原有的 OA 接口
    @GET("auth/me")
    suspend fun getCurrentUser(@Query("token") token: String): Response<UserInfoResponse>
    
    @POST("auth/change-password")
    suspend fun changePassword(@Query("token") token: String, @Body request: ChangePasswordRequest): Response<ApiResponse>
    
    // 打卡
    @POST("attendance/check")
    suspend fun checkIn(@Query("token") token: String, @Body request: CheckRequest): Response<CheckResponse>
    
    @GET("attendance/my-records")
    suspend fun getMyAttendance(@Query("token") token: String, @Query("startDate") startDate: String?, @Query("endDate") endDate: String?): Response<AttendanceRecordsResponse>
    
    @GET("attendance/today-status")
    suspend fun getTodayStatus(@Query("token") token: String): Response<TodayStatusResponse>
    
    @GET("attendance/department-stats")
    suspend fun getDepartmentStats(@Query("token") token: String, @Query("date") date: String?, @Query("department") department: String?): Response<DepartmentStatsResponse>
    
    // 请假
    @POST("leave/apply")
    suspend fun applyLeave(@Query("token") token: String, @Body request: LeaveApplyRequest): Response<LeaveApplyResponse>
    
    @GET("leave/my-requests")
    suspend fun getMyLeaveRequests(@Query("token") token: String, @Query("status") status: String?): Response<LeaveRequestsResponse>
    
    @GET("leave/pending-approval")
    suspend fun getPendingLeaveApproval(@Query("token") token: String): Response<LeaveRequestsResponse>
    
    @POST("leave/approve/{id}")
    suspend fun approveLeave(@Query("token") token: String, @Path("id") id: Int, @Body request: ApproveRequest): Response<ApiResponse>
    
    // 报销
    @POST("expense/apply")
    suspend fun applyExpense(@Query("token") token: String, @Body request: ExpenseApplyRequest): Response<ExpenseApplyResponse>
    
    @GET("expense/my-requests")
    suspend fun getMyExpenseRequests(@Query("token") token: String, @Query("status") status: String?): Response<ExpenseRequestsResponse>
    
    @GET("expense/pending-approval")
    suspend fun getPendingExpenseApproval(@Query("token") token: String): Response<ExpenseRequestsResponse>
    
    @POST("expense/approve/{id}")
    suspend fun approveExpense(@Query("token") token: String, @Path("id") id: Int, @Body request: ApproveRequest): Response<ApiResponse>
    
    // 员工
    @GET("employees/")
    suspend fun getEmployees(@Query("token") token: String, @Query("department") department: String?, @Query("status") status: String?, @Query("search") search: String?): Response<EmployeesResponse>
    
    @GET("employees/departments/list")
    suspend fun getDepartments(@Query("token") token: String): Response<DepartmentsResponse>
    
    // 上传
    @Multipart
    @POST("upload/single")
    suspend fun uploadFile(@Query("token") token: String, @Part file: okhttp3.MultipartBody.Part): Response<UploadResponse>
    
    @Multipart
    @POST("upload/multiple")
    suspend fun uploadFiles(@Query("token") token: String, @Part files: List<okhttp3.MultipartBody.Part>): Response<UploadResponse>
}

// 通用响应
data class ApiResponse(
    val message: String?,
    val error: String?
)