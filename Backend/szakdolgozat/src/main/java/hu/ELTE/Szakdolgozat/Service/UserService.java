package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.ActivityGroup;
import hu.ELTE.Szakdolgozat.Entity.Holiday;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Repository.ActivityGroupRepository;
import hu.ELTE.Szakdolgozat.Repository.ActivityRepository;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import hu.ELTE.Szakdolgozat.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private ActivityGroupRepository activityGroupRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    
    public Iterable<User> getAllUser(){
        
        Iterable<User> iUsers = this.userRepository.findByDeletedFalse();
        
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

            List<Holiday> deleteList = new ArrayList<>();
            for(Holiday h: u.getHolidays()){
                if(h.getDeleted()){
                   deleteList.add(h);
                }
            }
            for(Holiday h: deleteList){
                u.getHolidays().remove(h);
            }
        }
        return iUser;
    }

    public User adduser(User user){

        Optional<User> oUser = this.userRepository.findByEmailAndLoginName(user.getEmail(), user.getLoginName());
        if(oUser.isPresent()) return null;

        try{
            user.setId(null);
            user.setLastLogin(null);
            user.setDeleted(false);
            user.setPassword("");
            return this.userRepository.save(user);
        } catch(NullPointerException e){
            return null;
        }
    }

    public User deleteUser(Integer userId){

        Optional<User> oUser = this.userRepository.findById(userId);
        if(!oUser.isPresent()) return  null;

        oUser.get().setDeleted(true);
        this.userRepository.save(oUser.get());

        return oUser.get();
    }

    public User editUser(User user){

        Optional<User> oUser = this.userRepository.findByEmailAndLoginName(user.getEmail(), user.getLoginName());
        if(!oUser.isPresent()) return  null;

        oUser.get().setLastName(user.getLastName());
        oUser.get().setFirstName(user.getFirstName());
        oUser.get().setMaxHoliday(user.getMaxHoliday());
        oUser.get().setCanLogIn(user.getCanLogIn());

        return  this.userRepository.save(oUser.get());
    }

    public User login(){
        this.authenticatedUser.getUser().setLastLogin(new Date());
        return this.userRepository.save(this.authenticatedUser.getUser());
    }

    public User easyLogIn(User user, Integer activityGroupId){
        Optional<User> res = this.userRepository.findByLoginName(user.getLoginName());
        if(!res.isPresent()) return null;
        Optional<ActivityGroup> oActivityGroup = this.activityGroupRepository.findById(activityGroupId);
        if(!oActivityGroup.isPresent()) return null;

        Activity activity = new Activity();

        activity.setId(null);
        activity.setDate(new java.sql.Date(new Date().getTime()));
        activity.setIsCompleted(false);
        activity.setIsTask(false);
        activity.setMin(null);
        activity.setUser(res.get());
        activity.setOwner(res.get());
        activity.setActivityGroup(oActivityGroup.get());

        this.activityRepository.save(activity);

        return res.get();
    }

    public User getUserLoggedEver(String userName){
        Optional<User> oUser = this.userRepository.findByLoginName(userName);
        if(!oUser.isPresent()) return null;

        if(oUser.get().getPassword().equals("")){
            return oUser.get();
        } else {
            return null;
        }
    }

    public User setNotLoggedUserPassword(User user){
        Optional<User> oUser = this.userRepository.findById(user.getId());
        if(!oUser.isPresent()) return  null;

        oUser.get().setPassword(encoder.encode(user.getPassword()));
        return this.userRepository.save(oUser.get());
    }
}
