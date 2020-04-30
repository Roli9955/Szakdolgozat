package hu.ELTE.Szakdolgozat.Service;

import hu.ELTE.Szakdolgozat.AuthenticatedUser;
import hu.ELTE.Szakdolgozat.Class.ExcelMaker;
import hu.ELTE.Szakdolgozat.Entity.*;
import hu.ELTE.Szakdolgozat.Repository.HolidayRepository;
import hu.ELTE.Szakdolgozat.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
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
        Iterable<Holiday> iHoliday = this.holidayRepository.findByUserAndDeletedFalse(this.authenticatedUser.getUser());
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
        holiday.setDeleted(false);

        return this.holidayRepository.save(holiday);
    }

    public  Holiday deleteHoliday(Integer holidayId){
        Optional<Holiday> oHoliday = this.holidayRepository.findByIdAndDeletedFalse(holidayId);
        if(!oHoliday.isPresent()) return  null;
        oHoliday.get().setDeleted(true);
        this.holidayRepository.save(oHoliday.get());

        return oHoliday.get();
    }

    public Iterable<Holiday> getHolidays(){
        Iterable<Holiday> iHolidays = this.holidayRepository.findByDeletedFalse();
        for(Holiday h: iHolidays){
            h.setUserId(h.getUser().getId());
        }
        return iHolidays;
    }

    public ExcelMaker<Holiday> makeExcel(Integer userId, Integer year){
        Iterable<Holiday> iHoliday = null;
        Optional<User> oUser = null;
        Calendar cal = Calendar.getInstance();

        int code = 0;
        if(userId > -1){
            oUser = this.userRepository.findById(userId);
            code += 1;
        }
        if(year > -1){
            code += 2;
        }

        switch (code){
            case 1:
                iHoliday = this.holidayRepository.findByUserAndDeletedFalse(oUser.get());
                break;
            case 2:
                Iterable<Holiday> list = this.holidayRepository.findByDeletedFalse();
                List<Holiday> sorted = new ArrayList<>();
                for(Holiday h: list){
                    cal.setTime(h.getHolidayFrom());
                    if(cal.get(Calendar.YEAR) == year.intValue()){
                        sorted.add(h);
                    }
                }
                iHoliday = sorted;
                break;
            case 3:
                Iterable<Holiday> list2 =this.holidayRepository.findByUserAndDeletedFalse(oUser.get());
                List<Holiday> sorted2 = new ArrayList<>();
                for(Holiday h: list2){
                    cal.setTime(h.getHolidayFrom());
                    if(cal.get(Calendar.YEAR) == year.intValue()){
                        sorted2.add(h);
                    }
                }
                iHoliday = sorted2;
                break;
            default:
                iHoliday = this.holidayRepository.findByDeletedFalse();
                break;
        }

        return new ExcelMaker<Holiday>("Szabadság", Holiday.herder, iHoliday, Holiday.columns);
    }

}
