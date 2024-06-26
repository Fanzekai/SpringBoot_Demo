package com.example.springboot.demos.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author fzk
 * @version 1.0
 * @date 2024/5/8  12:03
 */
@RestController
@Slf4j
public class VisitController {

    @RequestMapping("/change-username")
    public String setCookie(HttpServletResponse response) {
        // 创建一个 cookie
        Cookie cookie = new Cookie("username", "Jovan");
        //设置 cookie过期时间
        cookie.setMaxAge(7 * 24 * 60 * 60); // expires in 7 days
        //添加到 response 中
        response.addCookie(cookie);

        return "Username is changed!";
    }

    @GetMapping("/")
    public String readCookie(@CookieValue(value = "username", defaultValue = "Atta") String username) {
        return "Hey! My username is " + username;
    }


    @GetMapping("/all-cookies")
    public String readAllCookies(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(", "));
        }

        return "No cookies";
    }

}
