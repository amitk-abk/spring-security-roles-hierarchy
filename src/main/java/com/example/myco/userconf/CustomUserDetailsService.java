package com.example.myco.userconf;

import com.example.myco.config.WebAppSecurityConfig;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final Map<String, UserDetails> detailsMap = new HashMap<>();

    private PasswordEncoder passwordEncoder;

    public CustomUserDetailsService() {
        passwordEncoder = WebAppSecurityConfig.passwordEncoder();
        this.detailsMap.put("user1", new SimpleUserDetails("user1", passwordEncoder.encode("password"), "ADMIN"));
        this.detailsMap.put("user2", new SimpleUserDetails("user2", passwordEncoder.encode("password"), "ENGINEER"));
        this.detailsMap.put("user3", new SimpleUserDetails("user3", passwordEncoder.encode("password"), "OPERATOR"));
        this.detailsMap.put("user4", new SimpleUserDetails("user4", passwordEncoder.encode("password"), "VIEWER"));
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (!this.detailsMap.containsKey(userName)){
            throw new UsernameNotFoundException("can not find user:" + userName);
        }
        return this.detailsMap.get(userName);
    }
}
