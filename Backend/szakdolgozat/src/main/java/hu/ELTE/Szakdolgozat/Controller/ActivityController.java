package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/activity")
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;
    
    @GetMapping("/year/{year}/month/{month}/day/{day}")
    public ResponseEntity<Iterable<Activity>> getActivitiesByDate(
            @PathVariable("year") Integer year,
            @PathVariable("month") Integer month,
            @PathVariable("day") Integer day
    ){
        Iterable<Activity> iActivity = this.activityService.getActivitiesByDate(year, month, day);
        return ResponseEntity.ok(iActivity);
        
    }
    
    @PostMapping("/new")
    public ResponseEntity<Activity> postAddNewActivity(@RequestBody Activity activity){
        Activity result = this.activityService.addNewActivity(activity);
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Activity> deleteActivity(@PathVariable("id") Integer activityId){
        Activity deleted = this.activityService.deleteActivity(activityId);
        if(deleted == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(deleted);
        }
    }
    
}
