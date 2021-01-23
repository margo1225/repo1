package Util;


import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class ExcelUtil {
    public static Logger logger =Logger.getLogger(String.valueOf(ExcelUtil.class));
    public XSSFWorkbook workbook; //整个文档对象
    public XSSFSheet sheet;  //代表excel的sheet
    public Row row; //行对象
    public Cell cell; //整个单元格

    public ExcelUtil(String filepath)  {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filepath));
            this.workbook =new XSSFWorkbook(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("读取excel文件出错"+e);
        }


    }



    //读取单元格数据
    public String getCellData(String sheetname, int rownum, int col){
        //先获取对应sheet对象
        sheet = workbook.getSheet(sheetname);
        //得到行对象
        row = sheet.getRow(rownum);
        cell=row.getCell(col);
        int cellType = cell.getCellType();
        String cellvalue ="";

        if(cell.getCellType()== XSSFCell.CELL_TYPE_BOOLEAN){
            cellvalue = String.valueOf(cell.getBooleanCellValue());
        }else if (cell.getCellType()==XSSFCell.CELL_TYPE_STRING){
            cellvalue = cell.getStringCellValue();
        }else if (cell.getCellType()==XSSFCell.CELL_TYPE_NUMERIC){
           short format=cell.getCellStyle().getDataFormat();

           if(HSSFDateUtil.isCellDateFormatted(cell)){
               Date d = cell.getDateCellValue();
               SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
               cellvalue = formater.format(d);
           }else if (format==14 || format ==31 ||format==57||format==58){
               //日期
               SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
               Date date =DateUtil.getJavaDate(cell.getNumericCellValue());
               cellvalue=formater.format(date);
           }else if (format==20||format==32){

               SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
               Date date=DateUtil.getJavaDate(cell.getNumericCellValue());
               cellvalue =formater.format(date);

           }else {
               DecimalFormat df =new DecimalFormat("0");
              cellvalue= df.format(cell.getNumericCellValue());
           }

        }else if(cell.getCellType()==XSSFCell.CELL_TYPE_BLANK){
            cellvalue ="";
        }
        logger.info("读取【"+sheetname+"】的第"+(rownum+1)+"行第"+(col+1)+"列的，值为:"+cellvalue);
        return cellvalue;




    }



    //读取所有的数据
    public Object[][] getSheetData(String sheetname){
       sheet = workbook.getSheet(sheetname);
       //获取总行数
        int lastRowNum = sheet.getLastRowNum();
        //获取总列数
        int colCount = sheet.getRow(0).getLastCellNum();
        System.out.println(colCount);
        System.out.println(lastRowNum);
        Object[][] data =new Object[lastRowNum][colCount];

        //遍历数据
        for (int i = 1; i <=lastRowNum; i++) {
            //获取单元格方法

            for (int j =0; j<colCount;j++) {
                String cellData = this.getCellData(sheetname, i, j);
                data[i-1][j] =cellData;

            }


        }
        return data;
    }
    public void close(){
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //获取路径
        String path = ExcelUtil.class.getResource("/data/mtxshop_data.xlsx").getPath();
        System.out.println(path);

        ExcelUtil excelUtil = new ExcelUtil(path);
        excelUtil.getCellData("提交订单",1,8);
        Object[][] data = excelUtil.getSheetData("提交订单");
        for (Object[] datum : data) {
                 for (Object o:datum ){
                     System.out.println(o);
                 }
        }
        excelUtil.close();
    }
}
