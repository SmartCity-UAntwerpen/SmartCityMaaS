package be.uantwerpen.services;

import be.uantwerpen.model.Permission;
import be.uantwerpen.model.Role;
import be.uantwerpen.model.User;
import be.uantwerpen.repositories.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 *  NV 2018
 */

@Service
public class SecurityService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(SecurityService.class);
        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
            UserDetails ud = null;
            for (User user : userRepository.findAll()){
                if (userName.equals(user.getUserName())){
                    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                    for (Role role : user.getRoles()) {
                        System.out.println(role.getName()); //TODO clean - gets printed when logged in - example "user"
                        for (Permission perm : role.getPermissions()){
                            authorities.add(new SimpleGrantedAuthority(perm.getName()));
                            System.out.println(role.getName()); //TODO clean - gets printed when logged in - example "user"
                        }
                    }

                    ud = new org.springframework.security.core.userdetails.User(userName, user.getPassword(), true, true, true, true,authorities);
                }
            }
            if (ud == null) throw new UsernameNotFoundException("No user with username '" + userName + "' found!");
            System.out.println(ud.getPassword()); //TODO clean - gets printed when logged in - example "passwoord123"
            return ud;
        }


}
