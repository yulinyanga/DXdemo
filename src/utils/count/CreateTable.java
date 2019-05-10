package utils.count;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.db.DBConnector;
import utils.model.SpecialWord;

public class CreateTable {
    public static void create(String serverTable,String columnname,HashMap<String,Integer> hashMapSpecialAll) {
        List<SpecialWord> list=new LinkedList<> ();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("������Ϣ�б�");
        XSSFRow header=sheet.createRow(0);
        header.createCell(0).setCellValue(columnname);
        header.createCell(1).setCellValue("��������");
        header.createCell(2).setCellValue("�����д�����");
        HashMap<String,Integer> hashMapAll=ConcatSiteName.getAllSiteNameData(serverTable);
        //HashMap<String,Integer> hashMapSpecialAll2=ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
        int g=1;
        for(String sitename:hashMapAll.keySet()) {
            XSSFRow header2=sheet.createRow(g);
            System.out.println("---------------------------------------------------"+g);
            System.out.println(hashMapAll.get(sitename));
            System.out.println(hashMapSpecialAll.get(sitename));
            header2.createCell(0).setCellValue(sitename+"");
            header2.createCell(1).setCellValue(Integer.parseInt(hashMapAll.get(sitename)+""));
            if(!"null".equals(hashMapSpecialAll.get(sitename)+"")) {
                header2.createCell(2).setCellValue(hashMapSpecialAll.get(sitename));
            }else {
                header2.createCell(2).setCellValue(0);
            }
            g++;
        }
        //�����еĿ��
        for(int i=0;i<header.getPhysicalNumberOfCells();i++){
            sheet.setColumnWidth(i, 255*20);
        }
        header.setHeightInPoints(30);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(DBConnector.countFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //��ָ���ļ�д������
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
    public static void createMore(String serverTable,String columnname,String twoColumnname,HashMap<String,Integer> hashMapSpecialAll) {
        List<SpecialWord> list=new LinkedList<>();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("������Ϣ�б�");
        XSSFRow header=sheet.createRow(0);
        header.createCell(0).setCellValue(columnname);
        header.createCell(1).setCellValue("��������");
        header.createCell(2).setCellValue("�����д�����");

        //���ҿ��Ӧ������վ������<sitename,����>
        HashMap<String,Integer> hashMapAll=ConcatSiteName.getAllSiteNameData(serverTable);
        int g=1;
        for(String sitename:hashMapAll.keySet()) {
            XSSFRow header2=sheet.createRow(g);
            System.out.println("---------------------------------------------------"+g);
            System.out.println(hashMapAll.get(sitename));
            System.out.println(hashMapSpecialAll.get(sitename));
            header2.createCell(0).setCellValue(sitename+"");

            header2.createCell(1).setCellValue(Integer.parseInt(hashMapAll.get(sitename)+""));
            if(!"null".equals(hashMapSpecialAll.get(sitename)+"")) {
                header2.createCell(2).setCellValue(hashMapSpecialAll.get(sitename));
            }else {
                header2.createCell(2).setCellValue(0);
            }
            g++;
        }
        //�����еĿ��
        for(int i=0;i<header.getPhysicalNumberOfCells();i++){
            sheet.setColumnWidth(i, 255*20);
        }
        header.setHeightInPoints(30);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("d:/������Ϣ����վͳ��.xlsx");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //��ָ���ļ�д������
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
