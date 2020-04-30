package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("")
public class MainController {
    
    @Autowired
    private UserService userService;

    @PutMapping("/login")
    @Secured({"ROLE_USER"})
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

    @GetMapping("/loged-ever/{username}")
    public ResponseEntity<User> getUserLoggedEver(@PathVariable("username") String userName){
        User user = this.userService.getUserLoggedEver(userName);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/set-not-logged-user-password")
    public ResponseEntity<User> setNotLoggedUserPassword(@RequestBody User user){
        User res = this.userService.setNotLoggedUserPassword(user);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }
    
}
