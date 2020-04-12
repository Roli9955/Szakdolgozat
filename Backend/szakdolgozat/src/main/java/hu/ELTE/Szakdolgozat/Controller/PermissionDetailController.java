package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Entity.PermissionDetail;
import hu.ELTE.Szakdolgozat.Service.PermissionDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/permission-detail")
public class PermissionDetailController {

    @Autowired
    private PermissionDetailService permissionDetailService;

    @GetMapping("")
    public ResponseEntity<Iterable<PermissionDetail>> getAll(){
        Iterable<PermissionDetail> iPermissionDetails = this.permissionDetailService.getAll();
        return ResponseEntity.ok(iPermissionDetails);
    }

}
