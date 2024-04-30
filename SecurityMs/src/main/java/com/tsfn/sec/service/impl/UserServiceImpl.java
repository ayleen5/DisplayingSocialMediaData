package com.tsfn.sec.service.impl;

 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tsfn.sec.model.Role;
import com.tsfn.sec.model.User;
import com.tsfn.sec.repository.UserRepository;
import com.tsfn.sec.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
	@Autowired
	private  UserRepository userRepository;
	
	
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
    
    public boolean hasAdminUser() {
    	List<User> users = userRepository.findAll();
        
        for (User user : users) {
            if (isAdmin(user)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isAdmin(User user) {
        for (Role role : user.getRoles()) {
            if (Role.ADMIN.name().equals(role.name())) {
                return true;
            }
        }
        return false;
    }

}
