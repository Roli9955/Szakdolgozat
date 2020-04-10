package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("")
    //@Secured({"ROLE_HOLIDAY_ADMIN"})
    public ResponseEntity<Iterable<User>> getUsers(){
        Iterable<User> iUser = this.userService.getAllUser();
        return ResponseEntity.ok(iUser);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user){
        User result = this.userService.adduser(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/holiday")
    public ResponseEntity<Iterable<User>> getUserForHoliday(){
        Iterable<User> iUser = this.userService.getUsersForHoliday();
        if(iUser == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(iUser);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Integer userId){
        User user = this.userService.deleteUser(userId);
        if(user == null){
            return ResponseEntity.badRequest().build();
        } else {
            return  ResponseEntity.ok(user);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<User> editUser(@RequestBody User user){
        User res = this.userService.editUser(user);
        if(res == null){
            return  ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }

}
