package hu.ELTE.Szakdolgozat.Class;

import hu.ELTE.Szakdolgozat.Entity.BasicEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelMaker<T extends BasicEntity> {

    private String sheetName;
    private String header[];
    private Iterable<T> data;
    private Integer columns;

    private String fileName;
    private File file;

    private Boolean delete;

    public ExcelMaker(String sheetName, String header[], Iterable<T> data, Integer columns){
        this.sheetName = sheetName;
        this.header = header;
        this.data = data;
        this.columns = columns;
        this.delete = false;

        this.make();
    }

    private void make(){
        try{

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(this.sheetName);

            Row headerRow = sheet.createRow(0);
            for(int i = 0; i < this.header.length; i++){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(this.header[i]);
            }

            int rows = 1;
            for(T data: this.data){
                Row row = sheet.createRow(rows++);
                for(int i = 0; i < this.columns; i++){
                    row.createCell(i).setCellValue(data.getData(i));
                }
            }

            for(int i = 0; i < this.columns; i++){
                sheet.autoSizeColumn(i);
            }

            DateFormat df = new SimpleDateFormat("yyMMdd_HHmmss.SSS");
            Date dateobj = new Date();
            this.fileName = "export_" + df.format(dateobj) + ".xlsx";

            try(FileOutputStream fileOutputStream = new FileOutputStream(this.fileName)){
                workbook.write(fileOutputStream);
            }
            workbook.close();

            this.file = new File(this.fileName);

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public File getFile(){
        return this.file;
    }

    public String getFileName(){
        return this.fileName;
    }

    public Boolean getDelete(){
        return this.delete;
    }

}
