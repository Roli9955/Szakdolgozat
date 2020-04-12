package hu.ELTE.Szakdolgozat.Repository;

import hu.ELTE.Szakdolgozat.Entity.Permission;
import hu.ELTE.Szakdolgozat.Entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
    
    public Optional<User> findByEmail(String email);
    public Optional<User> findByEmailAndLoginName(String email, String loginName);
    public Optional<User> findByLoginName(String loginName);
    public Iterable<User> findAllByOrderByLastName();
    public Iterable<User> findByDeletedFalse();
    public Iterable<User> findByPermissionAndDeletedFalse(Permission permission);
    
}
