package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.UserActivity;
import hu.ELTE.Szakdolgozat.Repository.ActivityRepository;
import hu.ELTE.Szakdolgozat.Repository.UserActivityRepository;
import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
    
    @Autowired
    private ActivityRepository activityRepository;
    
    @Autowired
    private UserActivityRepository userActivityRepository;
    
    @Autowired
    private AuthenticatedUser authenticatedUser;
    
    public Iterable<Activity> getActivitiesByDate(Integer year, Integer month, Integer day){
        
        Date date = Date.valueOf(year + "-" + month + "-" + day);
        
        Iterable<UserActivity> iUserActivities = this.userActivityRepository.findByUser(this.authenticatedUser.getUser());
        List<Activity> iActivity = new ArrayList();
        
        for(UserActivity ua : iUserActivities){
            if(ua.getActivity().getDate().equals(date)){
                iActivity.add(ua.getActivity());
            }
        }
        
        return iActivity;
    }
    
}
