package com.example.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
/**
 * jwt拦截器，用于在请求处理之前对JWT令牌进行验证
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    /**
     * 日志记录器，用于记录JwtInterceptor类相关的日志信息
     */
    public static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    /**
     * 注入AdminService服务，用于查询管理员账户信息
     */
    @Resource
    private AdminService adminService;
    /**
     * 注入EmployeeService服务，用于查询员工账户信息
     */
    @Resource
    private EmployeeService employeeService;

    /**
     * 在请求处理之前进行JWT令牌验证
     *
     * @param request  当前的HTTP请求
     * @param response 当前的HTTP响应
     * @param handler  处理请求的处理器
     * @return 如果验证通过返回true，否则抛出异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 从http请求的header中获取token
        String token = request.getHeader(Constants.TOKEN);
        if (ObjectUtil.isEmpty(token)) {
            // 如果没拿到，从参数里再拿一次
            token = request.getParameter(Constants.TOKEN);
        }
        // 2. 开始执行认证
        if (ObjectUtil.isEmpty(token)) {
            // 若仍未获取到token，抛出令牌无效异常
          //  throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR.getCode(), "Token 缺失或未提供");
        }
        // 用于存储从数据库中查询到的账户信息
        Account account = null;
        try {
            // 解析token获取存储的数据
            // 获取token中的受众信息，这里假设第一个受众信息包含用户角色和ID
            String userRole = JWT.decode(token).getAudience().get(0);
            // 从用户角色信息中拆分出用户ID
            String userId = userRole.split("-")[0];
            // 从用户角色信息中拆分出角色类型
            String role = userRole.split("-")[1];
            // 根据userId查询数据库
            if ("admin".equals(role)) {
                // 如果是管理员角色，调用AdminService查询账户信息
                account = adminService.selectById(Integer.valueOf(userId));
            }
            if ("employee".equals(role)) {
                // 如果是员工角色，调用EmployeeService查询账户信息
                account = employeeService.selectById(Integer.valueOf(userId));
            }

        } catch (Exception e) {
            // 解析token或查询数据库出错，抛出令牌验证错误异常
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);

        }
        if (ObjectUtil.isNull(account)) {
            // 若未查询到账户信息，抛出用户不存在异常
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        try {
            // 用户密码加签验证 token
            // 使用账户密码创建一个JWT验证器
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            // 验证token的有效性
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            // 令牌验证失败，抛出令牌验证错误异常
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        // 所有验证通过，返回true
        return true;
    }
}
