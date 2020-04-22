package hu.ELTE.Szakdolgozat.Controller;

import hu.ELTE.Szakdolgozat.Class.ExcelMaker;
import hu.ELTE.Szakdolgozat.Entity.Activity;
import hu.ELTE.Szakdolgozat.Entity.Holiday;
import hu.ELTE.Szakdolgozat.Service.HolidayService;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
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

    @GetMapping("")
    public ResponseEntity<Iterable<Holiday>> gettHolidays(){
        Iterable<Holiday> iHolidays = this.holidayService.getHolidays();
        return ResponseEntity.ok(iHolidays);
    }

    @GetMapping("/excel/user/{user}/year/{year}")
    public void makeExcel(
            HttpServletResponse response,
            @PathVariable("user") Integer userId,
            @PathVariable("year") Integer year
    ) throws Exception{
        ExcelMaker<Holiday> excel = this.holidayService.makeExcel(userId, year);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + excel.getFileName());

        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(excel.getFile());

        IOUtils.copy(in,out);

        out.close();
        in.close();
        excel.getFile().delete();
    }

}
