package com.example.upstdc.service;

import com.example.upstdc.model.Permission;
import com.example.upstdc.model.Role;
import com.example.upstdc.model.User;
import com.example.upstdc.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * PUBLIC_INTERFACE
 * Bridges domain User to Spring Security UserDetails.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Set<GrantedAuthority> authorities = u.getRoles().stream().flatMap((Role r) -> {
            Set<GrantedAuthority> roleAuth = Set.of(new SimpleGrantedAuthority("ROLE_" + r.getName()));
            Set<GrantedAuthority> permAuth = r.getPermissions().stream()
                    .map(Permission::getName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            return Set.copyOf(new java.util.ArrayList<GrantedAuthority>() {{
                addAll(roleAuth);
                addAll(permAuth);
            }}).stream();
        }).collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername())
                .password(u.getPasswordHash())
                .authorities(authorities)
                .disabled(!u.isEnabled())
                .build();
    }
}
