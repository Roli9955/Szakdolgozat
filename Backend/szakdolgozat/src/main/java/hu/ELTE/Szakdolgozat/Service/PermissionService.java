package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.Class.Result;
import hu.ELTE.Szakdolgozat.Entity.Permission;
import hu.ELTE.Szakdolgozat.Entity.PermissionDetail;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Repository.PermissionDetailRepository;
import hu.ELTE.Szakdolgozat.Repository.PermissionRepository;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionDetailRepository permissionDetailRepository;

    public Iterable<Permission> getAllPermission(){
        Iterable<Permission> iPermissions = this.permissionRepository.findAll();

        for(Permission p: iPermissions){
            p.setUserCount(p.getUsers().size());
        }

        return iPermissions;
    }

    public Permission addNewPermission(Permission permission){
        Optional<Permission> oPermission = this.permissionRepository.findByName(permission.getName());
        if(oPermission.isPresent()) return null;

        permission.setId(null);
        permission.setDetails(null);
        permission.setUsers(null);

        return this.permissionRepository.save(permission);
    }

    public Permission deletePermission(Integer permissionId){
        Optional<Permission> oPermission = this.permissionRepository.findById(permissionId);
        if(!oPermission.isPresent()) return null;

        for(User user: oPermission.get().getUsers()){
            user.setPermission(null);
            this.userRepository.save(user);
        }

        oPermission.get().setDetails(null);
        this.permissionRepository.delete(oPermission.get());

        return oPermission.get();
    }

    public Result<Permission, String> editPermission(Permission permission){
        Optional<Permission> oPermission = this.permissionRepository.findById(permission.getId());
        if(!oPermission.isPresent()){
            return new Result<Permission, String>(null, "Hiányzó jogosultsági csoport");
        }

        Optional<Permission> nameCheck = this.permissionRepository.findByName(permission.getName());
        if(nameCheck.isPresent()){
            return new Result<Permission, String>(null, "Ilyen névvel már van jogosultsági csoport");
        }

        oPermission.get().setName(permission.getName());
        return new Result<Permission, String>(this.permissionRepository.save(oPermission.get()), "");
    }

    public List<User> getUsersByPermissionId(Integer permissionId){
        Optional<Permission> oPermission = this.permissionRepository.findById(permissionId);
        if(!oPermission.isPresent()) return null;
        return oPermission.get().getUsers();
    }

    public Permission editPermissionDetail(Permission permission){
        Optional<Permission> oPermission = this.permissionRepository.findById(permission.getId());
        if(!oPermission.isPresent()) return null;

        List<PermissionDetail> list = new ArrayList<>();
        for(PermissionDetail pd: permission.getDetails()){
            Optional<PermissionDetail> oPermissionDetail = this.permissionDetailRepository.findById(pd.getId());
            if(oPermissionDetail.isPresent()){
                list.add(oPermissionDetail.get());
            }
        }
        oPermission.get().setDetails(list);
        return this.permissionRepository.save(oPermission.get());
    }

    public Permission editPermissionUsers(Integer permissionId, Iterable<User> users){
        Optional<Permission> oPermission = this.permissionRepository.findById(permissionId);
        if(!oPermission.isPresent()) return null;

        Iterable<User> iUsers = this.userRepository.findByPermissionAndDeletedFalse(oPermission.get());
        for (User u: iUsers){
            u.setPermission(null);
            this.userRepository.save(u);
        }

        for(User u: users){
            Optional<User> oUser = this.userRepository.findById(u.getId());
            if(!oUser.isPresent()) return null;

            oUser.get().setPermission(oPermission.get());
            this.userRepository.save(oUser.get());
        }

        return oPermission.get();
    }

}
