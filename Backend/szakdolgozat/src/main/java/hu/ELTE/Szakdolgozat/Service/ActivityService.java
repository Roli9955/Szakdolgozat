package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.ActivityGroup;
import hu.ELTE.Szakdolgozat.Entity.PermissionDetail;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.UserWorkGroup;
import hu.ELTE.Szakdolgozat.Repository.ActivityRepository;
import hu.ELTE.Szakdolgozat.Repository.PermissionDetailRepository;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import hu.ELTE.Szakdolgozat.Repository.UserWorkGroupRepository;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserWorkGroupRepository userWorkGroupRepository;

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionDetailRepository permissionDetailRepository;

    public Iterable<Activity> getActivitiesByDate(Integer year, Integer month, Integer day) {

        Date date = Date.valueOf(year + "-" + month + "-" + day);

        Iterable<Activity> iUserActivities = this.activityRepository.findByUser(this.authenticatedUser.getUser());
        List<Activity> iActivity = new ArrayList();

        for (Activity ua : iUserActivities) {
            if (ua.getDate().equals(date)) {
                ua.getWorkGroup().setActivityGroup(null);
                iActivity.add(ua);
            }
        }

        return iActivity;
    }

    public Activity addNewActivity(Activity activity) {

        Iterable<UserWorkGroup> iUserWorkGroup = this.userWorkGroupRepository.findByUser(this.authenticatedUser.getUser());

        for (UserWorkGroup uwg : iUserWorkGroup) {
            if (uwg.getWorkGroup().getId().equals(activity.getWorkGroup().getId())) {
                for (ActivityGroup ag : uwg.getWorkGroup().getActivityGroup()) {
                    if (ag.getId().equals(activity.getActivityGroup().getId())) {
                        activity.setId(null);
                        activity.setOwner(this.authenticatedUser.getUser());
                        activity.setActivityGroup(ag);
                        activity.setWorkGroup(uwg.getWorkGroup());
                        if (activity.getIsTask()) {
                            Optional<PermissionDetail> oPermission = this.permissionDetailRepository.findByRoleTag("ROLE_ADD_TASK");
                            if (!oPermission.isPresent()) {
                                return null;
                            }
                            boolean l = true;
                            for (PermissionDetail pd : this.authenticatedUser.getUser().getPermission().getDetails()) {
                                if (oPermission.get().getId().equals(pd.getId())) {
                                    if(activity.getUser() == null) return  null;
                                    Optional<User> oUser = this.userRepository.findById(activity.getUser().getId());
                                    if (!oUser.isPresent()) {
                                        return null;
                                    }
                                    activity.setUser(oUser.get());
                                    l = false;
                                    break;
                                }
                            }
                            if(l) return null;
                        } else {
                            activity.setUser(this.authenticatedUser.getUser());
                        }

                        Activity result = this.activityRepository.save(activity);
                        result.setUser(null);
                        result.setActivityGroup(null);
                        result.setWorkGroup(null);
                        return result;

                    }
                }

                break;
            }
        }

        return null;
    }
    
    public Activity deleteActivity(Integer activityId){
        
        Optional<Activity> oActivity = this.activityRepository.findById(activityId);
        if(!oActivity.isPresent()) return null;
        
        if(this.authenticatedUser.getUser().getId().equals(oActivity.get().getOwner().getId())){
            this.activityRepository.delete(oActivity.get());
            return oActivity.get();
        } else {
            return null;
        }
    }

}
