package com.example.myco.userconf;

import com.example.myco.config.WebAppSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final Map<String, UserDetails> detailsMap = new HashMap<>();

    private PasswordEncoder encoder;

    public CustomUserDetailsService() {
        encoder = WebAppSecurityConfig.passwordEncoder();
        this.detailsMap.put("user1", new SimpleUserDetails("user1", encoder.encode("password"), "ADMIN"));
        this.detailsMap.put("user2", new SimpleUserDetails("user2", encoder.encode("password"), "ENGINEER"));
        this.detailsMap.put("user3", new SimpleUserDetails("user3", encoder.encode("password"), "OPERATOR"));
        this.detailsMap.put("user4", new SimpleUserDetails("user4", encoder.encode("password"), "VIEWER"));
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (!this.detailsMap.containsKey(userName)){
            throw new UsernameNotFoundException("can not find user:" + userName);
        }
        return this.detailsMap.get(userName);
    }
}
