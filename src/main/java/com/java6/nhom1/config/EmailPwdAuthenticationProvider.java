package com.java6.nhom1.config;

import com.java6.nhom1.model.User;
import com.java6.nhom1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmailPwdAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Optional<User> result = userRepo.findByEmailEquals(email);
        if(result.isEmpty()){
            throw new BadCredentialsException("Không tìm thấy user");
        }
        User user = result.get();
        if(!passwordEncoder.matches(pwd, user.getPassword())){
            throw new BadCredentialsException("Mật khẩu không đúng");
        }
        user.setPassword(pwd);
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().getAuthority()));
        System.out.println("======================= Login thành công =======================");
        return new UsernamePasswordAuthenticationToken(user, pwd, roles);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
