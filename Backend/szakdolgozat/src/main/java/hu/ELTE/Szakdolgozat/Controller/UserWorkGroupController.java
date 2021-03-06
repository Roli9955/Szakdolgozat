package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Entity.UserWorkGroup;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Service.UserWorkGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/user-work-group")
public class UserWorkGroupController {
    
    @Autowired
    private UserWorkGroupService userWorkGroupService;
    
    @GetMapping("/year/{year}/month/{month}/day/{day}")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Iterable<WorkGroup>> getActivitiesByDate(
            @PathVariable("year") Integer year,
            @PathVariable("month") Integer month,
            @PathVariable("day") Integer day
    ){
        Iterable<WorkGroup> iWorkGroups = this.userWorkGroupService.getUserWorkGroupByDate(year, month, day);
        return ResponseEntity.ok(iWorkGroups);
    }

    @GetMapping("/user/{id}")
    @Secured({"ROLE_USER_ADMIN"})
    public  ResponseEntity<Iterable<UserWorkGroup>> getWorkGroupsByUser(@PathVariable("id") Integer userId){
        Iterable<UserWorkGroup> iUserWorkGroups = this.userWorkGroupService.getWorkGroupyByUser(userId);
        if(iUserWorkGroups == null){
            return ResponseEntity.badRequest().build();
        } else {
            return  ResponseEntity.ok(iUserWorkGroups);
        }
    }
    
}
