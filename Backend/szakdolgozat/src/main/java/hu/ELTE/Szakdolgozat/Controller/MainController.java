package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import hu.ELTE.Szakdolgozat.Service.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("")
public class MainController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/registration")
    public ResponseEntity<User> registration(@RequestBody User user){
        Optional<User> oUser = userRepository.findByLoginName(user.getLoginName());
        if(oUser.isPresent()){
            return ResponseEntity.notFound().build();
        }
        if(userService.isValidUser(user)){
            return ResponseEntity.ok(userRepository.save(user));
        } else {
            return ResponseEntity.badRequest().build();
        }
        
    }
    
}
