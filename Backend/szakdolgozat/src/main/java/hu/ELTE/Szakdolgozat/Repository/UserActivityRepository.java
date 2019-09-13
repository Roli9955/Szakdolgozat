package hu.ELTE.Szakdolgozat.Repository;

import hu.ELTE.Szakdolgozat.Entity.UserActivity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends CrudRepository<UserActivity, Integer> {
    
}
