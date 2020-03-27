package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.ActivityGroup;
import hu.ELTE.Szakdolgozat.Entity.PermissionDetail;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.UserWorkGroup;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Repository.ActivityGroupRepository;
import hu.ELTE.Szakdolgozat.Repository.ActivityRepository;
import hu.ELTE.Szakdolgozat.Repository.PermissionDetailRepository;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import hu.ELTE.Szakdolgozat.Repository.UserWorkGroupRepository;
import hu.ELTE.Szakdolgozat.Repository.WorkGroupRepository;
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
    
    @Autowired
    private WorkGroupRepository workGroupRepository;
    
    @Autowired
    private ActivityGroupRepository activityGroupRepository;

    public Iterable<Activity> getActivitiesByDate(Integer year, Integer month, Integer day) {

        Date date = Date.valueOf(year + "-" + month + "-" + day);

        Iterable<Activity> iUserActivities = this.activityRepository.findByOwner(this.authenticatedUser.getUser());
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
                        activity.setUser(this.authenticatedUser.getUser());
                        activity.setDeadline(null);
                        activity.setIsCompleted(false);
                        activity.setIsTask(false);

                        // For Task
                        /*if (activity.getIsTask()) {
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
                        }*/

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
    
    public Activity editActivity(Activity activity){
        
        Optional<Activity> oActivity = this.activityRepository.findById(activity.getId());
        if(!oActivity.isPresent()) return null;

        if(!this.authenticatedUser.getUser().getId().equals(oActivity.get().getOwner().getId())) return null;

        Optional<WorkGroup> oWorkGroup = this.workGroupRepository.findById(activity.getWorkGroup().getId());
        if(!oWorkGroup.isPresent()) return null;
        oActivity.get().setWorkGroup(oWorkGroup.get());

        boolean l = true;
        for(ActivityGroup ag: oWorkGroup.get().getActivityGroup()){
            if(activity.getActivityGroup().getId().equals(ag.getId())){
                oActivity.get().setActivityGroup(ag);
                l = false;
            }
        }
        if(l) return null;

        oActivity.get().setMin(activity.getMin());
        oActivity.get().setDescription(activity.getDescription());

        Activity result = this.activityRepository.save(oActivity.get());
        result.setUser(null);
        result.setActivityGroup(null);
        result.setWorkGroup(null);
        return result;

    }

}
