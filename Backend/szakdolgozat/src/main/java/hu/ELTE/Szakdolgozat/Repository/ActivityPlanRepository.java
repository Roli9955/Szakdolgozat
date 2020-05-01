package hu.ELTE.Szakdolgozat.Repository;

import hu.ELTE.Szakdolgozat.Entity.ActivityPlan;
import hu.ELTE.Szakdolgozat.Entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityPlanRepository extends CrudRepository<ActivityPlan, Integer> {
    public Iterable<ActivityPlan> findByUser(User user);
    public Iterable<ActivityPlan> findByOwnerOrderByDateDesc(User user);
}
