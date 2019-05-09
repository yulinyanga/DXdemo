package utils.count;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.xssf.usermodel.*;

import utils.db.DBConnector;
import utils.model.SpecialWord;

public class CreateTable {
    public static void create(String serverTable, String columnname, HashMap<String, Integer> hashMapSpecialAll) {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle contextstyle = wb.createCellStyle();
        XSSFDataFormat df = wb.createDataFormat();
        contextstyle.setDataFormat(df.getFormat("#,#0"));
        XSSFSheet sheet = wb.createSheet("疑似信息列表");
        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue(columnname);
        header.createCell(1).setCellValue("检索总量");
        header.createCell(2).setCellValue("疑敏感词总量");
        HashMap<String, Integer> hashMapAll = ConcatSiteName.getAllSiteNameData(serverTable);
        int g = 1;
        for (String sitename : hashMapAll.keySet()) {
            XSSFRow header2 = sheet.createRow(g);
            System.out.println("---------------------------------------------------" + g);
            System.out.println(hashMapAll.get(sitename));
            System.out.println(hashMapSpecialAll.get(sitename));
            header2.createCell(0).setCellValue(sitename + "");
            if (!"null".equals(hashMapAll.get(sitename) + "")) {
                header2.createCell(1).setCellStyle(contextstyle);
                header2.createCell(1).setCellValue(Integer.parseInt(hashMapAll.get(sitename)+""));
            } else {
                header2.createCell(1).setCellValue(0);
            }
            if (!"null".equals(hashMapSpecialAll.get(sitename) + "")) {
                header2.createCell(2).setCellValue(hashMapSpecialAll.get(sitename));
            } else {
                header2.createCell(2).setCellValue(0);
            }
            g++;
        }
        //设置列的宽度
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            sheet.setColumnWidth(i, 255 * 20);
        }
        header.setHeightInPoints(30);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(DBConnector.countFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //向指定文件写入内容
        try {
            wb.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createMore(String serverTable, String columnname, String twoColumnname, HashMap<String, Integer> hashMapSpecialAll) {
        List<SpecialWord> list = new ArrayList<>();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("疑似信息列表");
        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue(columnname);
        header.createCell(1).setCellValue("检索总量");
        header.createCell(2).setCellValue("疑敏感词总量");

        //查找库对应的所有站点数量<sitename,数量>
        HashMap<String, Integer> hashMapAll = ConcatSiteName.getAllSiteNameData(serverTable);
        int g = 1;
        for (String sitename : hashMapAll.keySet()) {
            XSSFRow header2 = sheet.createRow(g);
            System.out.println("---------------------------------------------------" + g);
            System.out.println(hashMapAll.get(sitename));
            System.out.println(hashMapSpecialAll.get(sitename));
            header2.createCell(0).setCellValue(sitename + "");
            header2.createCell(1).setCellValue(hashMapAll.get(sitename) + "");
            if (!"null".equals(hashMapSpecialAll.get(sitename) + "")) {
                header2.createCell(2).setCellValue(hashMapSpecialAll.get(sitename));
            } else {
                header2.createCell(2).setCellValue(0);
            }
            g++;
        }
        //设置列的宽度
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            sheet.setColumnWidth(i, 255 * 20);
        }
        header.setHeightInPoints(30);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(DBConnector.countFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //向指定文件写入内容
        try {
            wb.write(fos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
