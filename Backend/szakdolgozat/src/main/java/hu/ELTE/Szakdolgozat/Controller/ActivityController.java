package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Class.ExcelMaker;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Service.ActivityService;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;

@RestController
@CrossOrigin
@RequestMapping("/activity")
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;


    @GetMapping("/year/{year}/month/{month}/day/{day}")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Iterable<Activity>> getActivitiesByDate(
            @PathVariable("year") Integer year,
            @PathVariable("month") Integer month,
            @PathVariable("day") Integer day
    ){
        Iterable<Activity> iActivity = this.activityService.getActivitiesByDate(year, month, day);
        return ResponseEntity.ok(iActivity);
        
    }
    
    @PostMapping("/new")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Activity> postAddNewActivity(@RequestBody Activity activity){
        Activity result = this.activityService.addNewActivity(activity);
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/delete/{id}")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Activity> deleteActivity(@PathVariable("id") Integer activityId){
        Activity deleted = this.activityService.deleteActivity(activityId);
        if(deleted == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(deleted);
        }
    }
    
    @PutMapping("/edit")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Activity> editActivity(@RequestBody() Activity activity){
        Activity edited = this.activityService.editActivity(activity);
        if(edited == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(edited);
        }
    }

    @GetMapping("")
    @Secured({"ROLE_LISTING"})
    public ResponseEntity<Iterable<Activity>> getAllActivity(){
        Iterable<Activity> iActivities = this.activityService.getAllActivity();
        return ResponseEntity.ok(iActivities);
    }

    @GetMapping("/excel/project/{project}/activity-group/{activity-group}/user/{user}")
    @Secured({"ROLE_LISTING"})
    public void makeExcel(
            HttpServletResponse response,
            @PathVariable("project") Integer projectId,
            @PathVariable("activity-group") Integer activityGroupId,
            @PathVariable("user") Integer userId
    ) throws Exception{
        ExcelMaker<Activity> excel = this.activityService.makeExcel(projectId, activityGroupId, userId);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + excel.getFileName());

        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(excel.getFile());

        IOUtils.copy(in,out);

        out.close();
        in.close();
        excel.getFile().delete();
    }

    @GetMapping("/task/me")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Iterable<Activity>> getOwnActivity(){
        Iterable<Activity> iActivities = this.activityService.getOwnTasks();
        return ResponseEntity.ok(iActivities);
    }

    @PutMapping("/task/complete")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Activity> completeActivity(@RequestBody Activity activity){
        Activity res = this.activityService.completeActivity(activity);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @DeleteMapping("/delete/task/{id}")
    @Secured({"ROLE_USER"})
    public ResponseEntity<Activity> deleteTask(@PathVariable("id") Integer taskId){
        Activity activity = this.activityService.deleteTask(taskId);
        if(activity == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(activity);
        }
    }

    @PostMapping("/add/task")
    @Secured({"ROLE_USER", "ROLE_ADD_TASK"})
    public ResponseEntity<Activity> addtask(@RequestBody Activity activity){
        Activity res = this.activityService.addTask(activity);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }

}
