package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends UtilsService{
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthenticatedUser authenticatedUser;
    
    public Iterable<User> getAllUser(){
        
        Iterable<User> iUsers = this.userRepository.findAll();
        
        return iUsers;
    }

    public Iterable<User> getUsersForHoliday(){
        Iterable<User> iUser = this.userRepository.findAllByOrderByLastName();
        for(User u: iUser){
            u.setEmail(null);
            u.setLoginName(null);
            u.setPassword(null);
            u.setLastLogin(null);
            u.setCanLogIn(null);
            u.setPermission(null);
        }
        return iUser;
    }
}
