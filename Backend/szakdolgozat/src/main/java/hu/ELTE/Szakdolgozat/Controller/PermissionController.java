package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Class.Result;
import hu.ELTE.Szakdolgozat.Entity.Permission;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("")
    public ResponseEntity<Iterable<Permission>> getAllPermission(){
        Iterable<Permission> iPermissions = this.permissionService.getAllPermission();
        if(iPermissions == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(iPermissions);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Permission> addNewPermission(@RequestBody Permission permission){
        Permission res = this.permissionService.addNewPermission(permission);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Permission> deletePermission(@PathVariable("id") Integer permissionId){
        Permission permission = this.permissionService.deletePermission(permissionId);
        if(permission == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(permission);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<Object> editPermisson(@RequestBody Permission permission){
        Result<Permission, String> res = this.permissionService.editPermission(permission);
        if(res.getRes1() == null){
            return ResponseEntity.badRequest().body(res.getRes2());
        } else {
            return  ResponseEntity.ok(res.getRes1());
        }
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<User>> getUsers(@PathVariable("id") Integer permissionId){
        List<User> lUsers = this.permissionService.getUsersByPermissionId(permissionId);
        return ResponseEntity.ok(lUsers);
    }

    @PutMapping("/edit/permission-detail")
    public ResponseEntity<Permission> editPermissionDetail(@RequestBody Permission permission){
        Permission res = this.permissionService.editPermissionDetail(permission);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @PutMapping("/edit/{id}/user")
    public ResponseEntity<Permission> editPermissionUsers(
            @PathVariable("id") Integer permissionId,
            @RequestBody Iterable<User> users
    ){
        Permission permission = this.permissionService.editPermissionUsers(permissionId, users);
        if(permission == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(permission);
        }
    }

}
