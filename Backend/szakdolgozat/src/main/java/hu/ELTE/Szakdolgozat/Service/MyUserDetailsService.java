package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Optional<User> oUser = userRepository.findByEmail(email);

        if(!oUser.isPresent()){
            throw new UsernameNotFoundException(email);
        }

        User user = oUser.get();
        authenticatedUser.setUser(user);
        
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getPermission().toString()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);

    }
    
}
