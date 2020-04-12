package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.Entity.PermissionDetail;
import hu.ELTE.Szakdolgozat.Repository.PermissionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionDetailService {

    @Autowired
    private PermissionDetailRepository permissionDetailRepository;

    public Iterable<PermissionDetail> getAll(){
        return this.permissionDetailRepository.findAll();
    }

}
