package com.sen.haoke.manage.api.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Auther: Sen
 * @Date: 2020/2/14 22:59
 * @Description:
 */
@Component
public class RedisInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            //放行非get请求
            if (!StringUtils.equalsIgnoreCase(request.getRequestURI(), "/graphql")) {
                //并且不是GraphQL请求，本项目中存在GraphQL的POST请求
                return true;
            }
        }
        String redisKey = getRedisKey(request);
        String result = redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isEmpty(result)) {
            //缓存没命中放行请求后台
            return true;
        }
        //命中响应
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(result);
        return false;
    }


    /**
     * 获取redis缓存中的key
     * @param request
     * @return WEB_DATA+请求路径+请求参数
     */
    public static String getRedisKey(HttpServletRequest request){
        StringBuilder buffer = new StringBuilder("WEB_DATA");
        buffer.append(request.getRequestURI());
        //get和post的表单参数都可以获取
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.isEmpty()) {
            //参数表不为空
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                buffer.append(objectMapper.writeValueAsString(parameterMap));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else {
            try {
                //获取json格式传递的参数
                String param = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
                buffer.append(param);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }
}