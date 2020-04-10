package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.UserWorkGroup;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import hu.ELTE.Szakdolgozat.Repository.UserWorkGroupRepository;
import hu.ELTE.Szakdolgozat.Repository.WorkGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkGroupService {

    @Autowired
    private WorkGroupRepository workGroupRepository;

    @Autowired
    private UserWorkGroupRepository userWorkGroupRepository;

    @Autowired
    private UserRepository userRepository;

    public Iterable<WorkGroup> getWorkGroups(){
        Iterable<WorkGroup> iWorkGroups = this.workGroupRepository.findByDeletedFalse();
        for (WorkGroup wg : iWorkGroups){
            wg.setUserCount(wg.getUserWorkGroup().size());
            wg.setActivityGroupCount(wg.getActivityGroup().size());
        }
        return  iWorkGroups;
    }

    public WorkGroup addNewWorkGroup(WorkGroup workGroup){
        workGroup.setId(null);
        workGroup.setActivityGroup(null);
        workGroup.setDeleted(false);
        return this.workGroupRepository.save(workGroup);
    }

    public Iterable<UserWorkGroup> addUserWorkGroups(Integer workGroupId, Iterable<UserWorkGroup> userWorkGroups){
        Optional<WorkGroup> oWorkGroup = this.workGroupRepository.findByIdAndDeletedFalse(workGroupId);
        if(!oWorkGroup.isPresent()) return null;

        List<UserWorkGroup> userWorkGroupList = new ArrayList<>();
        for (UserWorkGroup uwg: userWorkGroups){
            Optional<User> oUser = this.userRepository.findById(uwg.getUser().getId());
            if(!oUser.isPresent()) continue;
            uwg.setId(null);
            uwg.setUser(oUser.get());
            userWorkGroupList.add(uwg);
        }

        if(userWorkGroupList.size() == 0) return null;

        Iterable<UserWorkGroup> res = this.userWorkGroupRepository.saveAll(userWorkGroupList);
        for(UserWorkGroup uwg: res){
            uwg.setWorkGroup(oWorkGroup.get());
            this.userWorkGroupRepository.save(uwg);
        }
        return  res;
    }

    public List<UserWorkGroup> getUsersInWorkGroup(Integer workGroupId){
        Optional<WorkGroup> oWorkGroup = this.workGroupRepository.findByIdAndDeletedFalse(workGroupId);
        if(!oWorkGroup.isPresent()) return null;

        return oWorkGroup.get().getUserWorkGroup();
    }

    public WorkGroup deleteWorkGroup(Integer workGroupId){
        Optional<WorkGroup> oWorkGroup = this.workGroupRepository.findByIdAndDeletedFalse(workGroupId);
        if(!oWorkGroup.isPresent()) return  null;

        oWorkGroup.get().setDeleted(true);
        return this.workGroupRepository.save(oWorkGroup.get());
    }

    public WorkGroup editWorkGroup(WorkGroup workGroup){
        Optional<WorkGroup> oWorkGroup = this.workGroupRepository.findByIdAndDeletedFalse(workGroup.getId());
        if(!oWorkGroup.isPresent()) return null;

        oWorkGroup.get().setName(workGroup.getName());
        oWorkGroup.get().setScale(workGroup.getScale());

        return this.workGroupRepository.save(oWorkGroup.get());
    }

    public List<UserWorkGroup> editUserWorkGroups(Integer workGroupId, Iterable<UserWorkGroup> userWorkGroups){
        Optional<WorkGroup> oWorkGroup = this.workGroupRepository.findByIdAndDeletedFalse(workGroupId);
        if(!oWorkGroup.isPresent()) return null;

        List<UserWorkGroup> list = oWorkGroup.get().getUserWorkGroup();
        oWorkGroup.get().setUserWorkGroup(null);
        for(UserWorkGroup uwg: list){
            this.userWorkGroupRepository.delete(uwg);
        }

        List<UserWorkGroup> userWorkGroupList = new ArrayList<>();
        for (UserWorkGroup uwg: userWorkGroups){
            Optional<User> oUser = this.userRepository.findById(uwg.getUser().getId());
            if(!oUser.isPresent()) continue;
            uwg.setId(null);
            uwg.setUser(oUser.get());
            uwg.setWorkGroup(oWorkGroup.get());
            userWorkGroupList.add(this.userWorkGroupRepository.save(uwg));
        }

        return userWorkGroupList;
    }
}
