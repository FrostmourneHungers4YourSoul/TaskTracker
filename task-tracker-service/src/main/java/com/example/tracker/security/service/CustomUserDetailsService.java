package com.example.tracker.security.service;

import com.example.tracker.domain.model.User;
import com.example.tracker.repository.UserRepository;
import com.example.tracker.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by username: " + username));
        return new SecurityUser(user);
    }

   public UserDetails loadUserById(Long id) {
       User user = repository.findById(id)
               .orElseThrow(() -> new UsernameNotFoundException("User not found by ID: " + id));
       return new SecurityUser(user);
   }
}
