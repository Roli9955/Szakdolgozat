package hu.ELTE.Szakdolgozat.Service;

import org.springframework.stereotype.Service;

@Service
public class UtilsService {
    
    public boolean isValidStrings(String[] strings){
        for(String s : strings){
            if(s.trim().equals("")) return false;
        }
        return true;
    }
    
    public boolean isValidEmail(String email){
        
        if(email.trim().equals("")) return false;
        
        String regex = "^(.+)@(.+)\\.(.+)$";
        
        if(!email.matches(regex)) return false;
        
        return true;
    }
    
}
