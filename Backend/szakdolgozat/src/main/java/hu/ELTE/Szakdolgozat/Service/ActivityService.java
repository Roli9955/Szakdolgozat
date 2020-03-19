package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Repository.ActivityRepository;
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
    private AuthenticatedUser authenticatedUser;
    
    public Iterable<Activity> getActivitiesByDate(Integer year, Integer month, Integer day){
        
        Date date = Date.valueOf(year + "-" + month + "-" + day);
        
        Iterable<Activity> iUserActivities = this.activityRepository.findByUser(this.authenticatedUser.getUser());
        List<Activity> iActivity = new ArrayList();
        
        for(Activity ua : iUserActivities){
            if(ua.getDate().equals(date)){
                ua.getWorkGroup().setActivityGroup(null);
                iActivity.add(ua);
            }
        }
        
        return iActivity;
    }
    
}
