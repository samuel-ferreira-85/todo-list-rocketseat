package com.samuel.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.samuel.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks")) {
            String authorization = request.getHeader("Authorization");

            String authEncoded = authorization.substring("Basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(authEncoded);

            var authString = new String(authDecode);

            String[] credentials = authString.split(":");

            var userName = credentials[0];
            var password = credentials[1];

            var user = userRepository.findByUserName(userName);
            if (user == null)
                response.sendError(401);
            else {
                Result passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerify.verified) {
                    request.setAttribute("userId", user.getId());
                    filterChain.doFilter(request, response);
                }
                    
                else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }

        

    }

}
