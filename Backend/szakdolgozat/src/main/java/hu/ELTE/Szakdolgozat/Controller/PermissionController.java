package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/permission")
public class PermissionController {
    
    @Autowired
    private PermissionRepository permissionRepository;
    
}
