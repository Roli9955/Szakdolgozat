package hu.ELTE.Szakdolgozat.Repository;

import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Entity.UserWorkGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWorkGroupRepository extends CrudRepository<UserWorkGroup, Integer> {
    
    Iterable<UserWorkGroup> findByUser(User user);
    
}
