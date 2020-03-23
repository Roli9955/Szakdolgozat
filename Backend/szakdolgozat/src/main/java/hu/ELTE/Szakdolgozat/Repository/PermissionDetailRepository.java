package hu.ELTE.Szakdolgozat.Repository;

import hu.ELTE.Szakdolgozat.Entity.PermissionDetail;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionDetailRepository extends CrudRepository<PermissionDetail, Integer> {
    public Optional<PermissionDetail> findByRoleTag(String roleTag);
}
