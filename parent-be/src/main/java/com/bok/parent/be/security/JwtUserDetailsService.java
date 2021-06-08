package com.bok.parent.be.security;

import com.bok.parent.be.helper.AccountHelper;
import com.bok.parent.model.Account;
import com.bok.parent.model.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    AccountHelper accountHelper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account a = accountHelper.findByEmail(username);
        Credentials c = a.getCredentials();
        return new User(c.getEmail(), c.getPassword(), new ArrayList<>());
    }


}