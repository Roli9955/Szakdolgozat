package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.ActivityPlan;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Repository.ActivityPlanRepository;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ActivityPlanService {

    @Autowired
    private ActivityPlanRepository activityPlanRepository;

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private UserRepository userRepository;

    public Iterable<ActivityPlan> getUsersPlans(){
        Iterable<ActivityPlan> activityPlans = this.activityPlanRepository.findByUser(this.authenticatedUser.getUser());
        return activityPlans;
    }

    public ActivityPlan addNewPlan(ActivityPlan activityPlan){
        activityPlan.setId(null);
        activityPlan.setOwner(this.authenticatedUser.getUser());
        if(activityPlan.getUser() != null){
            Optional<User> oUser = this.userRepository.findById(activityPlan.getUser().getId());
            if(!oUser.isPresent()) return null;
            activityPlan.setUser(oUser.get());
        } else {
            activityPlan.setUser(this.authenticatedUser.getUser());
        }
        return this.activityPlanRepository.save(activityPlan);
    }

    public ActivityPlan deletePlan(Integer planId){
        Optional<ActivityPlan> oActivityPlan = this.activityPlanRepository.findById(planId);
        if(!oActivityPlan.isPresent()) return null;

        if(!oActivityPlan.get().getOwner().getId().equals(this.authenticatedUser.getUser().getId())) return null;

        this.activityPlanRepository.delete(oActivityPlan.get());
        return oActivityPlan.get();
    }

    public ActivityPlan editPlan(ActivityPlan activityPlan){
        Optional<ActivityPlan> oActivityPlan = this.activityPlanRepository.findById(activityPlan.getId());
        if(!oActivityPlan.isPresent()) return null;

        if(!oActivityPlan.get().getOwner().getId().equals(this.authenticatedUser.getUser().getId())) return null;

        oActivityPlan.get().setDate(activityPlan.getDate());
        oActivityPlan.get().setTitle(activityPlan.getTitle());
        oActivityPlan.get().setDescription(activityPlan.getDescription());

        return this.activityPlanRepository.save(oActivityPlan.get());
    }

    public Iterable<ActivityPlan> getOwnedPlans(){
        Iterable<ActivityPlan> activityPlans = this.activityPlanRepository.findByOwnerOrderByDateDesc(this.authenticatedUser.getUser());
        return activityPlans;
    }

}
