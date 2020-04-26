package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.PermissionDetail;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Repository.PermissionDetailRepository;
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
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        
        Optional<User> oUser = userRepository.findByLoginName(loginName);

        if(!oUser.isPresent()){
            throw new UsernameNotFoundException(loginName);
        }

        User user = oUser.get();
        authenticatedUser.setUser(user);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        if(user.getPermission() == null){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            for(PermissionDetail p : user.getPermission().getDetails()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(p.getRoleTag()));
            }
        }

        return new org.springframework.security.core.userdetails.User(user.getLoginName(), user.getPassword(), grantedAuthorities);

    }

}
