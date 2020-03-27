package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Holiday;
import hu.ELTE.Szakdolgozat.Repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HolidayService {

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private HolidayRepository holidayRepository;

    public Iterable<Holiday> getHolidayByUser(){
        Iterable<Holiday> iHoliday = this.holidayRepository.findByUser(this.authenticatedUser.getUser());
        return iHoliday;
    }

}
