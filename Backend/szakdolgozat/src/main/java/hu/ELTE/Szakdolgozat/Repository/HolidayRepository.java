package hu.ELTE.Szakdolgozat.Repository;

import hu.ELTE.Szakdolgozat.Entity.Holiday;
import hu.ELTE.Szakdolgozat.Entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends CrudRepository<Holiday, Integer> {
    public Iterable<Holiday> findByUser(User user);
}
