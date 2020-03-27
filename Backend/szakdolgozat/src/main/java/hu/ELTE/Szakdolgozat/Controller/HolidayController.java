package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Entity.Holiday;
import hu.ELTE.Szakdolgozat.Service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/holiday")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @GetMapping("/me")
    public ResponseEntity<Iterable<Holiday>> getHolidayByUser(){
        Iterable<Holiday> iHoliday = this.holidayService.getHolidayByUser();
        return  ResponseEntity.ok(iHoliday);
    }

}
