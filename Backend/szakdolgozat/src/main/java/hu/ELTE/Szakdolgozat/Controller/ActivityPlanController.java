package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Entity.ActivityPlan;
import hu.ELTE.Szakdolgozat.Service.ActivityPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/activity-plan")
public class ActivityPlanController {

    @Autowired
    private ActivityPlanService activityPlanService;

    @GetMapping("/me")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Iterable<ActivityPlan>> getUsersPlan(){
        Iterable<ActivityPlan> activityPlans = this.activityPlanService.getUsersPlans();
        return ResponseEntity.ok(activityPlans);
    }

    @PostMapping("/add")
    @Secured({"ROLE_USER", "ROLE_ACTIVITY_PLAN_ADMIN"})
    public ResponseEntity<ActivityPlan> addNewPlan(@RequestBody ActivityPlan activityPlan){
        ActivityPlan res = this.activityPlanService.addNewPlan(activityPlan);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Secured({"ROLE_USER", "ROLE_ACTIVITY_PLAN_ADMIN"})
    public ResponseEntity<ActivityPlan> deletePlan(@PathVariable("id") Integer planId){
        ActivityPlan activityPlan = this.activityPlanService.deletePlan(planId);
        if(activityPlan == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(activityPlan);
        }
    }

    @PutMapping("/edit")
    @Secured({"ROLE_USER"})
    public ResponseEntity<ActivityPlan> editPlan(@RequestBody ActivityPlan activityPlan){
        ActivityPlan res = this.activityPlanService.editPlan(activityPlan);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @GetMapping("/me/owned")
    @Secured({"ROLE_ACTIVITY_PLAN_ADMIN"})
    public ResponseEntity<Iterable<ActivityPlan>> getOwnedPlans(){
        Iterable<ActivityPlan> activityPlans = this.activityPlanService.getOwnedPlans();
        return ResponseEntity.ok(activityPlans);
    }

}
