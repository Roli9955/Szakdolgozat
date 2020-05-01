package hu.ELTE.Szakdolgozat.Repository;

import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.ActivityGroup;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Integer> {
    
    public Iterable<Activity> findByOwner(User user);
    public Iterable<Activity> findByUserAndIsTaskTrueAndIsCompletedFalseOrderByDeadline(User user);
    public Iterable<Activity> findByOwnerAndIsTaskTrueOrderByDeadlineDesc(User user);

    public Iterable<Activity> findByWorkGroup(WorkGroup workGroup);
    public Iterable<Activity> findByActivityGroup(ActivityGroup activityGroup);
    public Iterable<Activity> findByUser(User user);
    public Iterable<Activity> findByWorkGroupAndActivityGroup(WorkGroup workGroup, ActivityGroup activityGroup);
    public Iterable<Activity> findByWorkGroupAndUser(WorkGroup workGroup, User user);
    public Iterable<Activity> findByActivityGroupAndUser(ActivityGroup activityGroup, User user);
    public Iterable<Activity> findByWorkGroupAndActivityGroupAndUser(WorkGroup workGroup, ActivityGroup activityGroup, User user);

}
