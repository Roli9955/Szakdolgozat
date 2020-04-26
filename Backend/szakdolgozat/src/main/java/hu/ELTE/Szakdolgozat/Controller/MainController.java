package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("")
public class MainController {
    
    @Autowired
    private UserService userService;

    @PutMapping("/login")
    public ResponseEntity<User> login(){
        User res = this.userService.login();
        return ResponseEntity.ok(res);
    }

    @PostMapping("/easy-log-in/activity-group/{id}")
    public ResponseEntity<User> easyLogIn(
            @RequestBody User user,
            @PathVariable("id") Integer activityGroupId
    ){
        User res = this.userService.easyLogIn(user, activityGroupId);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }
    
}
