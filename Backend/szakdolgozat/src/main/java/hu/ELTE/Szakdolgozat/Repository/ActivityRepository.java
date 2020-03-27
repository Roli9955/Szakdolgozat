package hu.ELTE.Szakdolgozat.Repository;

import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Integer> {
    
    public Iterable<Activity> findByOwner(User user);
    
}
