package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.Class.Result;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.ActivityGroup;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import hu.ELTE.Szakdolgozat.Repository.ActivityGroupRepository;
import hu.ELTE.Szakdolgozat.Repository.WorkGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityGroupService {

    @Autowired
    private ActivityGroupRepository activityGroupRepository;

    @Autowired
    private WorkGroupRepository workGroupRepository;

    public Iterable<ActivityGroup> getAllActivityGroup(){
        return this.activityGroupRepository.findByDeletedFalse();
    }

    public Result<ActivityGroup, String> addNewActivityGroup(ActivityGroup activityGroup){
        List<ActivityGroup> iActivityGroup = this.activityGroupRepository.findByNameAndDeletedFalse(activityGroup.getName());
        if(iActivityGroup.size() > 0){
            if(activityGroup.getParent() == null){
                return new Result<ActivityGroup, String>(null, "Ilyen néven már létezik feladat");
            }
            Optional<ActivityGroup> oParent = this.activityGroupRepository.findByIdAndDeletedFalse(activityGroup.getParent().getId());
            if(!oParent.isPresent()){
               return new Result<ActivityGroup, String>(null, "Ilyen néven már létezik feladat és nem megfelelő szülő");
            }
            if(!oParent.get().getCanAddChild()){
                return new Result<ActivityGroup, String>(null, "A szülő nem bontható");
            }
            for(ActivityGroup ag: iActivityGroup){
                if(ag.getParent() != null){
                    if(oParent.get().getId() == ag.getParent().getId()) {
                        return new Result<ActivityGroup, String>(null, "Ilyen néven már létezik feladat a kiválasztott szülő alatt");
                    }
                }
            }
            activityGroup.setId(null);
            activityGroup.setParent(oParent.get());
            return new Result<ActivityGroup, String>(this.activityGroupRepository.save(activityGroup), "");
        } else {
            activityGroup.setId(null);
            if(activityGroup.getParent() != null){
                Optional<ActivityGroup> oParent = this.activityGroupRepository.findByIdAndDeletedFalse(activityGroup.getParent().getId());
                if(!oParent.isPresent()) return null;
                if(!oParent.get().getCanAddChild()) return null;
                activityGroup.setParent(oParent.get());
            }
            return new Result<ActivityGroup, String>(this.activityGroupRepository.save(activityGroup), "");
        }
    }

    public List<ActivityGroup> deleteActivityGroup(Integer activityGroupId){
        Optional<ActivityGroup> oActivityGroup = this.activityGroupRepository.findByIdAndDeletedFalse(activityGroupId);
        if(!oActivityGroup.isPresent()) return null;

        Iterable<ActivityGroup> all = this.activityGroupRepository.findByDeletedFalse();
        List<ActivityGroup> tree = new ArrayList<>();
        tree.add(oActivityGroup.get());
        inorderTree(tree, oActivityGroup.get(), all);

        for (ActivityGroup ag: tree){
            ag.setDeleted(true);
            for(WorkGroup wg: ag.getWorkGroup()){
                wg.getActivityGroup().remove(ag);
                this.workGroupRepository.save(wg);
            }
            ag.setWorkGroup(null);
            this.activityGroupRepository.save(ag);
        }

        return tree;
    }

    public void inorderTree(List<ActivityGroup> tree, ActivityGroup root, Iterable<ActivityGroup> all){
        for(ActivityGroup ag: all){
            if(ag.getId().equals(root.getId())){
                continue;
            }
            if(ag.getParent() == null){
                continue;
            }
            if(ag.getParent().getId().equals(root.getId())){
                tree.add(ag);
                inorderTree(tree, ag, all);
            }
        }
    }

    public WorkGroup editWorkGroupActivityGroups(WorkGroup workGroup){
        Optional<WorkGroup> oWorkGroup = this.workGroupRepository.findByIdAndDeletedFalse(workGroup.getId());
        if(!oWorkGroup.isPresent()) return null;

        oWorkGroup.get().setActivityGroup(null);
        List<ActivityGroup> list = new ArrayList<>();
        for(ActivityGroup wg: workGroup.getActivityGroup()){
            Optional<ActivityGroup> oActivityGroup = this.activityGroupRepository.findByIdAndDeletedFalse(wg.getId());
            if(oActivityGroup.isPresent()){
                list.add(oActivityGroup.get());
            }
        }
        oWorkGroup.get().setActivityGroup(list);

        return this.workGroupRepository.save(oWorkGroup.get());
    }

}
