package com.codesight.codesight_api.infrastructure.configuration.security;

import com.codesight.codesight_api.domain.user.entity.User;
import com.codesight.codesight_api.domain.user.repository.UserRepository;
import com.codesight.codesight_api.domain.user.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            LOGGER.error("USER NOT FOUND BY EMAIL: " + email);
            throw new UsernameNotFoundException("USER NOT FOUND BY EMAIL: " + email);
        }else {
            LOGGER.info("USER FOUND IN THE DATABASE WITH EMAIL: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
