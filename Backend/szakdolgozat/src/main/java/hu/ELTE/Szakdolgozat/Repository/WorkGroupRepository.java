package hu.ELTE.Szakdolgozat.Repository;

import hu.ELTE.Szakdolgozat.Entity.WorkGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkGroupRepository extends CrudRepository<WorkGroup, Integer> {
    
}
