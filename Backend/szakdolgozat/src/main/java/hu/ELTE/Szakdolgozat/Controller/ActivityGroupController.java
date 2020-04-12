package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Class.Result;
import hu.ELTE.Szakdolgozat.Entity.ActivityGroup;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Repository.ActivityGroupRepository;
import hu.ELTE.Szakdolgozat.Service.ActivityGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/activity-group")
public class ActivityGroupController {

    @Autowired
    private ActivityGroupService activityGroupService;

    @GetMapping("")
    public ResponseEntity<Iterable<ActivityGroup>> getAllActivityGroup(){
        Iterable<ActivityGroup> iActivityGroup = this.activityGroupService.getAllActivityGroup();
        return ResponseEntity.ok(iActivityGroup);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addNewActivityGroup(@RequestBody ActivityGroup activityGroup){
        Result<ActivityGroup, String> res = this.activityGroupService.addNewActivityGroup(activityGroup);
        if(res.getRes1() == null){
            return ResponseEntity.badRequest().body(res.getRes2());
        } else {
            return ResponseEntity.ok(res.getRes1());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<ActivityGroup>> deleteActivityGroup(@PathVariable("id") Integer activityGroupId){
        List<ActivityGroup> res = this.activityGroupService.deleteActivityGroup(activityGroupId);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @PutMapping("/edit/work-group")
    public ResponseEntity<WorkGroup> editWorkGroupActivityGroups(@RequestBody WorkGroup workGroup){
        WorkGroup res = this.activityGroupService.editWorkGroupActivityGroups(workGroup);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }


}
