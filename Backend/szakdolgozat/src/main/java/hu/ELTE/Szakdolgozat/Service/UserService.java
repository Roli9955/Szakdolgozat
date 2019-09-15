package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.Entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService extends UtilsService{
    
    public boolean isValidUser(User user){
        
        try{
            if(user == null) return false;
        } catch (NullPointerException e){
            return false;
        }
        System.out.println("aasdasd");
        user.setId(null);
        if(!this.isValidStrings(new String[]{user.getFirstName(), user.getLastName(),user.getLoginName(), user.getPassword()})) return false;
        if(!this.isValidEmail(user.getEmail())) return false;
        user.setLastLogin(null);
        user.setCanLogIn(Boolean.TRUE);
        
        return true;
    }
    
}
