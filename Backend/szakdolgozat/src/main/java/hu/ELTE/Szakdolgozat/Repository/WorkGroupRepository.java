package hu.ELTE.Szakdolgozat.Repository;

import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkGroupRepository extends CrudRepository<WorkGroup, Integer> {
    public Iterable<WorkGroup> findByDeletedFalse();
    public Optional<WorkGroup> findByIdAndDeletedFalse(Integer id);
}
