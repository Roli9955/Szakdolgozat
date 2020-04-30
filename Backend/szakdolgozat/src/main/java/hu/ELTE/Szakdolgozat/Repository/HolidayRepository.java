package hu.ELTE.Szakdolgozat.Repository;

import hu.ELTE.Szakdolgozat.Entity.Holiday;
import hu.ELTE.Szakdolgozat.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HolidayRepository extends CrudRepository<Holiday, Integer> {
    public Iterable<Holiday> findByUserAndDeletedFalse(User user);
    public Iterable<Holiday> findByDeletedFalse();
    public Optional<Holiday> findByIdAndDeletedFalse(Integer id);

    @Query(value = "SELECT YEAR(HOLIDAY_FROM) AS YEAR FROM HOLIDAY GROUP BY YEAR(HOLIDAY_FROM) ORDER BY YEAR DESC", nativeQuery = true)
    public List<Integer> getYears();
}
