package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Entity.Holiday;
import hu.ELTE.Szakdolgozat.Service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/year")
    public ResponseEntity<List<Integer>> getYears(){
        List<Integer> years = this.holidayService.getYears();
        return  ResponseEntity.ok(years);
    }

    @PostMapping("/add/user/{id}")
    public ResponseEntity<Holiday> addNewHolidayByUserId(@PathVariable("id") Integer userId, @RequestBody Holiday holiday){
        Holiday res = this.holidayService.addNewHolidayByUserId(userId, holiday);
        if(res == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(res);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Holiday> deleteHoliday(@PathVariable("id") Integer holidayId){
        Holiday holiday = this.holidayService.deleteHoliday(holidayId);
        if(holiday == null){
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(holiday);
        }
    }

}
