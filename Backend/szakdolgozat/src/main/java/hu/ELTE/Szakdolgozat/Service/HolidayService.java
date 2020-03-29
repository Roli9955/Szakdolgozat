package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Entity.Holiday;
import hu.ELTE.Szakdolgozat.Entity.User;
import hu.ELTE.Szakdolgozat.Repository.HolidayRepository;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HolidayService {

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private UserRepository userRepository;

    public Iterable<Holiday> getHolidayByUser(){
        Iterable<Holiday> iHoliday = this.holidayRepository.findByUser(this.authenticatedUser.getUser());
        return iHoliday;
    }

    public List<Integer> getYears(){
        List<Integer> years = this.holidayRepository.getYears();
        return years;
    }

    public Holiday addNewHolidayByUserId(Integer userId, Holiday holiday){

        Optional<User> oUser = this.userRepository.findById(userId);
        if(!oUser.isPresent()) return null;

        if(holiday.getHolidayFrom() == null || holiday.getHolidayTo() == null){
            return null;
        }

        holiday.setId(null);
        holiday.setUser(oUser.get());

        return this.holidayRepository.save(holiday);
    }

    public  Holiday deleteHoliday(Integer holidayId){
        Optional<Holiday> oHoliday = this.holidayRepository.findById(holidayId);
        if(!oHoliday.isPresent()) return  null;
        this.holidayRepository.delete(oHoliday.get());

        return oHoliday.get();
    }

}
