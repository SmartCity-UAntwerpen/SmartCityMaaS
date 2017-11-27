package be.uantwerpen.services;

import be.uantwerpen.model.Permission;
import be.uantwerpen.model.Role;
import be.uantwerpen.model.User;
import be.uantwerpen.repositories.UserRepository;
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
 * Created by dries on 29/05/2017.
 */
@Service
public class SecurityService implements UserDetailsService {
//    @Autowired
//    UserService userService;
//    @Autowired
//    PermissionService permissionService;
//
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        UserDetails ud = null;
//        User user = userService.findByUserName(userName);
//        if(user != null){
//            Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//            authorities.addAll(StreamSupport.stream(permissionService.findAllForUser(user).spliterator(),false)
//                    .map(perm -> new SimpleGrantedAuthority(perm.getName())).collect(Collectors.toList()));
//
//
//            ud = new org.springframework.security.core.userdetails.User(userName, user.getPassword(), true,
//                    true, true, true,authorities);
//
//        }
//        else throw new UsernameNotFoundException("No user with username '" + userName + "' found!");
//        return ud;
//    }

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
            UserDetails ud = null;
            for (User user : userRepository.findAll()){
                if (userName.equals(user.getUserName())){
                    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                    for (Role role : user.getRoles()) {
                        System.out.println(role.getName());
                        for (Permission perm : role.getPermissions()){
                            authorities.add(new SimpleGrantedAuthority(perm.getName()));
                            System.out.println(role.getName());
                        }
                    }

                    ud = new org.springframework.security.core.userdetails.User(userName, user.getPassword(), true, true, true, true,authorities);
                }
            }
            if (ud == null) throw new UsernameNotFoundException("No user with username '" + userName + "' found!");
            System.out.println(ud.getPassword());
            return ud;
        }


}
