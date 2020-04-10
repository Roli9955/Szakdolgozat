package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.UserWorkGroup;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Service.WorkGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/work-group")
public class WorkGroupController {

    @Autowired
    private WorkGroupService workGroupService;

    @GetMapping("")
    public ResponseEntity<Iterable<WorkGroup>> getWorkGroups(){
        Iterable<WorkGroup> iWorkGroups = this.workGroupService.getWorkGroups();
        return ResponseEntity.ok(iWorkGroups);
    }

    @PostMapping("/add")
    public ResponseEntity<WorkGroup> addNewWorkGroup(@RequestBody WorkGroup workGroup){
        WorkGroup res = this.workGroupService.addNewWorkGroup(workGroup);
        if(res == null){
            return ResponseEntity.badRequest().build();
        }  else {
            return  ResponseEntity.ok(res);
        }
    }

    @PostMapping("/add/{id}/user-work-group")
    public ResponseEntity<Iterable<UserWorkGroup>> addUserWorkGroups(
            @PathVariable("id") Integer workGroupId,
            @RequestBody Iterable<UserWorkGroup> userWorkGroups)
    {
        Iterable<UserWorkGroup> res = this.workGroupService.addUserWorkGroups(workGroupId, userWorkGroups);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserWorkGroup>> getUsersInWorkGroup(@PathVariable("id") Integer workGroupId){
        List<UserWorkGroup> users = this.workGroupService.getUsersInWorkGroup(workGroupId);
        if(users == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WorkGroup> deleteWorkGroup(@PathVariable("id") Integer workGroupId){
        WorkGroup workGroup = this.workGroupService.deleteWorkGroup(workGroupId);
        if(workGroup == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(workGroup);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<WorkGroup> editWorkGroup(@RequestBody WorkGroup workGroup){
        WorkGroup res = this.workGroupService.editWorkGroup(workGroup);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return  ResponseEntity.ok(res);
        }
    }

    @PutMapping("/edit/{id}/user-work-group")
    public ResponseEntity<List<UserWorkGroup>> editUserWorkGroupInProject(
            @PathVariable("id") Integer workGroupId,
            @RequestBody Iterable<UserWorkGroup> userWorkGroups
    ){
        List<UserWorkGroup> res = this.workGroupService.editUserWorkGroups(workGroupId, userWorkGroups);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }
}
