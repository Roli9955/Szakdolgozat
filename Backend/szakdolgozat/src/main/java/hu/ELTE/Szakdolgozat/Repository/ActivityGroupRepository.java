package hu.ELTE.Szakdolgozat.Repository;

import hu.ELTE.Szakdolgozat.Entity.ActivityGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityGroupRepository extends CrudRepository<ActivityGroup, Integer> {
    public List<ActivityGroup> findByNameAndDeletedFalse(String name);
    public Iterable<ActivityGroup> findByDeletedFalse();
    public Optional<ActivityGroup> findByIdAndDeletedFalse(Integer id);
}
