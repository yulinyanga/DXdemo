package utils.count;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.eprobiti.trs.TRSConnection;
import com.eprobiti.trs.TRSException;
import com.eprobiti.trs.TRSResultSet;

import utils.db.DBConnector;
import utils.model.*;

public class CountSensitiveWords {
    DBConnector db = new DBConnector();
    TRSConnection conn = null;
    TRSResultSet rs = null;

    /**
     * ͳ�ư������дʼ�¼������
     *
     * @param tableName
     * @param sensitiveWords
     * @param isContinue
     * @return ����
     */
    public long count(String tableName, String sensitiveWords, boolean isContinue) {
        long num = 0;
        conn = db.getDBConnection();
        try {
            rs = new TRSResultSet();
            rs = conn.executeSelect(tableName, sensitiveWords, isContinue);
            num = rs.getRecordCount();
            //for (int i = 0; i < 3 && i < rs.getRecordCount(); i++){
            for (int i = 0; i < rs.getRecordCount(); i++) {
                rs.moveTo(0, i);
                System.out.println();
                System.out.println("��" + i + "����¼");
                //System.out.println(rs.getString("Y_ID"));
                //String words = rs.getString("TRSCONTENT","red");
                //System.out.println(rs.getString("id"));
                //String words = rs.getString("content","red");
                System.out.println(rs.getString("IR_SID"));
                String words = rs.getString("IR_URLCONTENT", "red");
                String[] wordsArray = words.split("<font color=red>");
                String searchWords = "";
                for (int j = 1; j < wordsArray.length; j++) {
                    int index = wordsArray[j].indexOf("</font>");
                    wordsArray[j] = wordsArray[j].substring(0, index);
                    searchWords += wordsArray[j] + ",";
                }
                System.out.println(searchWords);
                System.out.println("���дʳ��ֵĴ�����" + sameWordNums(searchWords));
            }
        } catch (TRSException e) {
            System.out.println("ErrorCode: " + e.getErrorCode());
            System.out.println("ErrorString: " + e.getErrorString());
        } finally {
            db.closeConnection(conn);
        }
        return num;
    }

    /**
     * �������дʵ������Ϣ����ϸ��Ϣ
     *
     * @param tableName
     * @param sensitiveWords
     * @param isContinue
     * @param
     * @return ��ϸ��Ϣ
     * @throws IOException
     */
    public List searchMessage(String tableName, String sensitiveWords, boolean isContinue) throws IOException {
        long num = 0;
        conn = db.getDBConnection();
        //���е�������Ϣ�б�
        List list = null;
        try {
            rs = new TRSResultSet();
            rs = conn.executeSelect(tableName, sensitiveWords, isContinue);
            System.out.println(sensitiveWords);
            num = rs.getRecordCount();
            //ѭ�����������Ϣ
            String log = "";
            String column = ConcatSiteName.getColumnName(tableName);
            String column3 = ConcatSiteName.getColumnName3(tableName);
            if ("IR_HKEY".equals(column)) {
                list = new ArrayList<WeiXin>();
                for (int i = 0; i < rs.getRecordCount(); i++) {
                    rs.moveTo(0, i);
                    WeiXin weiXin = new WeiXin();
                    weiXin.setFileName(rs.getString("IR_AUTHORS"));
                    weiXin.setIR_URLNAME(rs.getString("IR_URLNAME"));
                    weiXin.setIR_WEIXINID(rs.getString("IR_WEIXINID"));
                    weiXin.setIR_CONTENT(rs.getString("IR_CONTENT"));
                    String words = rs.getString("IR_CONTENT", "red");
                    String[] wordsArray = words.split("<font color=red>");
                    String searchWords = "";
                    for (int j = 1; j < wordsArray.length; j++) {
                        int index = wordsArray[j].indexOf("</font>");
                        wordsArray[j] = wordsArray[j].substring(0, index);
                        searchWords += wordsArray[j] + ",";
                    }
                    weiXin.setKeyword(sameWordNums(searchWords));
                    list.add(weiXin);
                }
            }
            if ("IR_SID".equals(column)) {
                list = new ArrayList<SpecialWord>();
                for (int i = 0; i < rs.getRecordCount(); i++) {
                    SpecialWord specialWord = new SpecialWord();

                    rs.moveTo(0, i);
                    String id = rs.getString("IR_SID");
                    specialWord.setId(id);
                    String words = rs.getString("IR_URLCONTENT", "red");
                    specialWord.setContent(words);
                    String url = rs.getString("IR_URLNAME");
                    specialWord.setUrl(url);
                    String siteName = rs.getString("IR_SITENAME");
                    specialWord.setSiteName(siteName);
                    specialWord.setChannel(rs.getString("IR_CHANNEL"));
                    String[] wordsArray = words.split("<font color=red>");
                    String searchWords = "";
                    String groupname = rs.getString("IR_GROUPNAME");
                    specialWord.setGroupname(groupname);
                    for (int j = 1; j < wordsArray.length; j++) {
                        int index = wordsArray[j].indexOf("</font>");
                        wordsArray[j] = wordsArray[j].substring(0, index);
                        searchWords += wordsArray[j] + ",";
                    }
                    specialWord.setKeyword(sameWordNums(searchWords));
                    System.out.println("���дʳ��ֵĴ�����" + sameWordNums(searchWords));
                    list.add(specialWord);
                    log += specialWord.toString() + "\r\n" + "\n" + "\n";
                }
            } else if ("IR_AUTHORS".equals(column)) {        //����΢������
                list = new ArrayList<SpecialWord>();
                for (int i = 0; i < rs.getRecordCount(); i++) {
                    SpecialWord specialWord = new SpecialWord();
                    rs.moveTo(0, i);
                    String id = rs.getString("IR_SID");
                    specialWord.setId(id);
                    String words = rs.getString("IR_URLCONTENT", "red");
                    specialWord.setContent(words);
                    String url = rs.getString("IR_URLNAME");
                    specialWord.setUrl(url);
                    String siteName = rs.getString("IR_SITENAME");
                    specialWord.setSiteName(siteName);
                    String[] wordsArray = words.split("<font color=red>");
                    String searchWords = "";
                    String groupname = rs.getString("IR_GROUPNAME");
                    specialWord.setGroupname(groupname);
                    for (int j = 1; j < wordsArray.length; j++) {
                        int index = wordsArray[j].indexOf("</font>");
                        wordsArray[j] = wordsArray[j].substring(0, index);
                        searchWords += wordsArray[j] + ",";
                    }
                    specialWord.setKeyword(sameWordNums(searchWords));
                    list.add(specialWord);
                    log += specialWord.toString() + "\r\n" + "\n" + "\n";
                }
            }else if("IR_CREATED_AT".equals(column)) {		//����΢������
                list=new LinkedList();
                for (int i = 0; i < rs.getRecordCount(); i++){
                    WeiBuo weibo=new WeiBuo();
                    rs.moveTo(0, i);
                    String id= rs.getString("IR_MID");
                    weibo.setIR_MID(id);
                    String sitename= rs.getString("IR_SITENAME");
                    weibo.setIR_SITENAME(sitename);
                    String IR_SCREEN_NAME= rs.getString("IR_SCREEN_NAME");
                    weibo.setIR_SCREEN_NAME(IR_SCREEN_NAME);
                    String url= rs.getString("IR_URLNAME");
                    weibo.setIR_URLNAME(url);
                    String words = rs.getString("IR_STATUS_CONTENT","red");
                    weibo.setIR_STATUS_CONTENT(words);
                    String[] wordsArray = words.split("<font color=red>");
                    String searchWords = "";
                    for(int j=1; j<wordsArray.length; j++){
                        int index = wordsArray[j].indexOf("</font>");
                        wordsArray[j] = wordsArray[j].substring(0,index);
                        searchWords += wordsArray[j]+",";
                    }
                    weibo.setKeyword(sameWordNums(searchWords));
                    list.add(weibo);
                    log+=weibo.toString()+"\r\n"+"\n"+"\n";
                }
            } else if ("FILENAME".equals(column)) {
                list = new ArrayList();
                for (int i = 0; i < rs.getRecordCount(); i++) {
                    WangPan wangPan = new WangPan();
                    wangPan.setIp(rs.getString("IP"));
                    wangPan.setFilepath(rs.getString("FILEPATH"));
                    wangPan.setType(rs.getString("TYPE"));
                    log += wangPan.toString() + "\r\n" + "\n" + "\n";
                    list.add(wangPan);
                }
            } else if ("id".equals(column)) {
                list = new ArrayList<JCMS>();
                for (int i = 0; i < rs.getRecordCount(); i++) {
                    rs.moveTo(0, i);
                    JCMS jcms = new JCMS();
                    jcms.setDB_TYPE(rs.getString("DB_TYPE"));
                    jcms.setSID(rs.getString("id"));
                    jcms.setSIP(rs.getString("SIP"));
                    jcms.setSPORT(rs.getString("SPORT"));
                    jcms.setTB_NAME(rs.getString("TB_NAME"));
                    jcms.setY_ID(rs.getString("MID"));
                    jcms.setTrscontent(rs.getString("TRSCONTENT"));
                    jcms.setDb_name(rs.getString("DB_NAME"));
                    String words = rs.getString("TRSCONTENT", "red");
                    String[] wordsArray = words.split("<font color=red>");
                    String searchWords = "";
                    for (int j = 1; j < wordsArray.length; j++) {
                        int index = wordsArray[j].indexOf("</font>");
                        wordsArray[j] = wordsArray[j].substring(0, index);
                        searchWords += wordsArray[j] + ",";
                    }
                    jcms.setKeyword(sameWordNums(searchWords));
                    list.add(jcms);
                }
            } else if ("FileName".equals(column)) {
                list = new ArrayList<FIleModel>();
                for (int i = 0; i < rs.getRecordCount(); i++) {
                    rs.moveTo(0, i);
                    FIleModel fIleModel = new FIleModel();
                    fIleModel.setFilename(rs.getString("FileName"));
                    fIleModel.setFilepath(rs.getString("FilePath"));
                    fIleModel.setIp(rs.getString("ip"));
                    String words = rs.getString("WP_Content", "red");
                    String[] wordsArray = words.split("<font color=red>");
                    String searchWords = "";
                    for (int j = 1; j < wordsArray.length; j++) {
                        int index = wordsArray[j].indexOf("</font>");
                        wordsArray[j] = wordsArray[j].substring(0, index);
                        searchWords += wordsArray[j] + ",";
                    }
                    fIleModel.setKey(sameWordNums(searchWords));
                    System.out.println("���дʳ��ֵĴ�����" + sameWordNums(searchWords));
                    list.add(fIleModel);
                }
            }else if ("FilePath".equals(column)) {
                list=new LinkedList<FIleModel>();
                for (int i = 0; i < rs.getRecordCount(); i++){
                    rs.moveTo(0, i);
                    FIleModel fIleModel=new FIleModel();
                    fIleModel.setFilename(rs.getString("FileName"));
                    fIleModel.setFilepath(rs.getString("FilePath"));
                    fIleModel.setIp(rs.getString("ip"));
                    String words = rs.getString("WP_Content","red");
                    String[] wordsArray = words.split("<font color=red>");
                    String searchWords = "";
                    for(int j=1; j<wordsArray.length; j++){
                        int index = wordsArray[j].indexOf("</font>");
                        wordsArray[j] = wordsArray[j].substring(0,index);
                        searchWords += wordsArray[j]+",";
                    }
                    fIleModel.setKey(sameWordNums(searchWords));
                    System.out.println("���дʳ��ֵĴ�����" + sameWordNums(searchWords));
                    list.add(fIleModel);
                }
            }else if ("Y_ID".equals(column)) {
                list = new ArrayList<JCMS>();
                for (int i = 0; i < rs.getRecordCount(); i++) {
                    rs.moveTo(0, i);
                    JCMS jcms = new JCMS();
                    jcms.setDB_TYPE(rs.getString("DB_TYPE"));
                    jcms.setSID(rs.getString("Y_ID"));
                    jcms.setSIP(rs.getString("SIP"));
                    jcms.setSPORT(rs.getString("SPORT"));
                    jcms.setTB_NAME(rs.getString("TB_NAME"));
                    jcms.setY_ID(rs.getString("MID"));
                    jcms.setTrscontent(rs.getString("TRSCONTENT"));
                    jcms.setDb_name(rs.getString("DB_NAME"));
                    String words = rs.getString("TRSCONTENT", "red");
                    String[] wordsArray = words.split("<font color=red>");
                    String searchWords = "";
                    for (int j = 1; j < wordsArray.length; j++) {
                        int index = wordsArray[j].indexOf("</font>");
                        wordsArray[j] = wordsArray[j].substring(0, index);
                        searchWords += wordsArray[j] + ",";
                    }
                    jcms.setKeyword(sameWordNums(searchWords));
                    list.add(jcms);
                }
                /*list=new LinkedList<FIleModel>();
                for (int i = 0; i < rs.getRecordCount(); i++){
                    rs.moveTo(0, i);
                    System.out.println("i" + i + "rs.getRecordCount()" + rs.getRecordCount());
                    AuthorModel authorModel = new AuthorModel();
                    authorModel.setAuthor_id(rs.getString("author_id"));
                    authorModel.setAuthor_name(rs.getString("author"));
                    authorModel.setMid(rs.getString("MID"));
                    authorModel.setSip(rs.getString("SIP"));
                    String words = rs.getString("trscontent", "red");
                    String[] wordsArray = words.split("<font color=red>");
                    String searchWords = "";
                    for (int j = 1; j < wordsArray.length; j++) {
                        int index = wordsArray[j].indexOf("</font>");
                        wordsArray[j] = wordsArray[j].substring(0, index);
                        searchWords += wordsArray[j] + ",";
                    }
                    authorModel.setKeyword(sameWordNums(searchWords));
                    list.add(authorModel);
                }*/
            }

            File file = new File(DBConnector.logPath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(log.getBytes());
            fileOutputStream.close();
        } catch (TRSException e) {
            System.out.println("ErrorCode: " + e.getErrorCode());
            System.out.println("ErrorString: " + e.getErrorString());
        } finally {
            db.closeConnection(conn);
        }
        return list;
    }

    /**
     * ��ȡ��ӦĿ¼���ı������е����д�
     *
     * @param filePath
     * @return �ı������е����д�
     */
    public static String getSensitiveWords(String filePath) {
        String sensitiveWords = "";
        try (FileReader reader = new FileReader(filePath);
             BufferedReader br = new BufferedReader(reader)) {
            sensitiveWords = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sensitiveWords;
    }

    /**
     * ÿ����¼������ͬ���дʵĴ���
     *
     * @param searchWords
     * @return ��ͬ���дʼ�¼
     */
    public static String sameWordNums(String searchWords) {
        String[] string = searchWords.split(",");
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < string.length; i++) {
            if (map.get(string[i]) == null) {
                map.put(string[i], 1);
            } else {
                map.put(string[i], map.get(string[i]) + 1);
            }
        }
        return map.toString();
    }

    public static String sameChar(String searchWords) {
        String string = searchWords;
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < string.length(); i++) {
            Character character = new Character(string.charAt(i));
            if (map.get(character) == null) {
                map.put(character, 1);
            } else {
                map.put(character, map.get(character) + 1);
            }
        }
        return map.toString();
    }

    /**
     * ��ȡָ�����ݿ�����д���Ϣ
     */
    public void searchAllMessage() {
        CountSensitiveWords csw = new CountSensitiveWords();
        long recordNum = 0;
        long recordTotal = 0;
        String serverTable = "AS$6$10$1";
        String[] where = {"(IR_SITENAME=���Ź�˾����) and (IR_URLCONTENT=", "(IR_SITENAME=OAϵͳ��ҳ������������Ŀ) and (IR_URLCONTENT=", "(IR_SITENAME=%������Ϣ�ۺϹ���ƽ̨%) and (IR_URLCONTENT=", "(IR_SITENAME=oAϵͳ��������ҳ��) and (IR_URLCONTENT="};
        String[] filePath = new String[]{"D://chinese.txt", "D://english.txt", "D://traditional20190419.txt"};
        List<SpecialWord> list = new ArrayList<>();

        for (int j = 0; j < where.length; j++) {
            for (int i = 0; i < filePath.length; i++) {
                try {
                    list = csw.searchMessage(serverTable, where[j] + getSensitiveWords(filePath[i]) + ")", false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("==============================================================================");
                //				System.out.println("����Ϊ  " + serverTable + " ���վ��Ϊ" + where[j] + "�ڴ��ļ��ġ�" + filePath[i] + "�����дʷ�Χ�ڣ��������� " + recordNum + "����¼");
                for (int z = 0; z < list.size(); z++) {
                    SpecialWord specialWord = list.get(z);
                    System.out.println("list����ϸ" + list.get(z).toString());
                }

            }
        }
    }

    /**
     * �����������Ϊ������������Ϣ��ϸ�б�
     */
    public static void detail() throws IOException {
        CountSensitiveWords csw = new CountSensitiveWords();
        String serverTable = DBConnector.serverTable;
        String[] filePath = new String[]{DBConnector.biaodashi};
        String[] where = ConcatSiteName.getAllSiteNAme(serverTable);
        //ʹ�����ݿ�ĵ������������ݿ�
        String column = ConcatSiteName.getColumnName(serverTable);
        String column3 = ConcatSiteName.getColumnName3(serverTable);
        //�������ݿ���ж�
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("������Ϣ�б�");
        XSSFRow header = sheet.createRow(0);
        int bugNum = 0;
        if ("IR_HKEY".equals(column)) {
            List<WeiXin> list = new ArrayList<>();
            header.createCell(0).setCellValue("���");
            header.createCell(1).setCellValue("΢��id");
            header.createCell(2).setCellValue("������");
            header.createCell(3).setCellValue("���ӵ�ַ");
//            header.createCell(3).setCellValue("�ı�����");
            header.createCell(4).setCellValue("������");
            //������쳣������
            int q = 0;
                    list = csw.searchMessage(serverTable, DBConnector.siteList+"IR_CONTENT=%"+getSensitiveWords(filePath[0])+"%", false);
//                    list = csw.searchMessage(serverTable, "IR_AUTHORS=(���ͷ�չ��ȫ������˾�Ĵ����´�,�����麣֮��,���ͷ�չ������ˮϵͳ���޹�˾,����΢Ѷ,����΢ͥ,��ȫ�����ſ�,���ͷ�չEAP,���Ͱ���������,���ͷ�չ,���̼�����˾,δ��ֱ��,�к�����Ϣ�Ƽ����޹�˾,���۷���˾,���ͷ�չ��ͷ���˾,ӡ�����,������ѡ����,��Ͳɹ�ʳ��,���ͷ�չ������˾,�к�����Դ��չ�ܵ����̷ֹ�˾,�к��ͣ���򣩹ܵ����̼������޹�˾,Ժ֪Ժζ��,PCEC,��ҵˮ����,�޻��ι�ҵ,�к��ͳ���Ժ,��Ϳ��,����Ϳ�������ල��������,Ϳ�Ϲ�ҵ,��ĩѧ��,�к���ҵ,���ͷ�չ�Ϻ���������,���ͷ�չ������˾) AND (Boxiong,Caihou,Choronology,Dongliang,Hualin,Huanning,Jiemin,Jihua,Keping,Lixin,NuerBaikeli,Qingshan,Shulin,SuRong,Tianpu,Tienan,Tonghai,Weizhong,Wenjin,Xiaolin,Xilai,Xingguo,Xinhua,Xiyou,Xizhao,Xuezhong,YangJing,Yongchun,Yongkang,Yongyuan,Zhenfang,Zhengcai,������,������,�׶���,�׿���,����Ⱥ,��ѩɽ,������,����,�ź�,��ϣ��,�ܰ���,��־Ȩ,��־Ȩ,��С��,�°���,�°ػ�,�´�ƽ,�´���,�¸�,�¹���,����¡,������,��ͬ��,���Ľ�,����,��ѩ��,���ʷ�,����,���,����ͬ־,������,������,����ͬ־,����ͬ־,�����,�ſ�ƽ,����ѧ,����,�����,������,������,�߽���,�߽���,�����,�ȴ���,���˳�,�ص�,������,����,������,������,���ȴ�,��ѧ��,�μҳ�,�ؼ���,��ΰͬ־,��־ͬ־,������,������,����ͬ־,����,�Ʊ���,���˹�,����,�ƻ�ͬ־,����ҵ,����,������,�Ҳ�ͬ־,����,����,������,������,����,�����,������,�����ں�,����,��ƽ,������,����,������,��С��,�ִ��,���綫,�����,�����,�����,���,���,�����,���,���,���,���,������,����,��ʿ��,���Ŀ�,���»�,���ݻ�,���Ʒ�,���,��־��,����ͬ־,����ͬ־,����,����,���ٻ�,����Զ,������,��ƻ�,������,������,������,����,��ǿ,������,������,������,������,��־��,¬����,¬��Ծ,³�,½���,�޻���,��ΰ��,���Ƿ�,������,��,ëС��,�Ϻ�ΰ,��ΰ,�紺��,������,�߷���,������,������,Ŭ�����׿���,������,�Ѳ�,������ֵ,��ƽ��,����,��ɽͬ־,�����,�����,�����,���ܺ�,����,�ٴ�,��ά��,����ƽ,ʱϣƽ,����ͬ־,˾����,˹����,����,�պ���,����,������,��︻,�ﲨ,���־,�ﻳɽ,����ѧ,������,����������,̸��,̷��,̷��ΰ,������,����,����,ͬ��ͬ־,ͯ��ǫ,������,������,������,���ӽ�,����,���꽭,����,������,������,������,����,����,������,��˧��,������,������,����,����,������,������,�����,���޻�,����,������,������,���Ƹ�,��־��,ά��,ΰ��,κ����,κ��,κ����,κ־��,����ɽ,�Ľ�ͬ־,��ΰ,�����,���,����,�䳤˳,������������,ϣ��,������,����,����,ϲ��,�İ�Ȼ,�ĳ�Դ,���˻�,���,Ф��,С��ͬ־,����ͬ־,�»�ͬ־,����������,�˹�ͬ־,������,��Ծ��,����,��ź�,���,�콨һ,������,����,���,��ǰ��,���ں�,Ѧ��,ѧ��,������,�����,�ƽ,���,���,�����,��Ҳ�,�,����,��³ԥ,��ɭ��,����ة,��˼��,������,������,����,Ҧ��,Ҧľ��,Ҧ����,������,����ͬ־,����,��Զͬ־,���,��Զ��,�ݺ���,����,Ԭ�ǿ�,�ű�ʤ,�Ÿ���,�Ż�Ϊ,�Žܻ�,������,���ֱ�,������,������,���ٴ�,������,������,��ϲ��,����,������,��Խ,����Ӣ,���ǽ�,�³���,����ƽ,������,������,������,��Ż�,��,֣����,����,��������ί�����,�������ξ�ίԱ��,�������ξ�ίԱ��,������,������ί��Ǳ�,������ί�����,�ܱ�˳,�ܴ���,�ܾ�,������,��ѧ��,������,�����,������,ף����,���»�,�ع���)", false);
                    for (int z = 1; z < list.size() + 1; z++) {
                        q = q + 1;
                        WeiXin weiXin = list.get(z - 1);
                        XSSFRow header2 = sheet.createRow(q);
                        header2.createCell(0).setCellValue(q);
                        header2.createCell(1).setCellValue(weiXin.getIR_WEIXINID());
                        header2.createCell(2).setCellValue(weiXin.getFileName());
                        header2.createCell(3).setCellValue(weiXin.getIR_URLNAME());
//                        header2.createCell(3).setCellValue(weiXin.getIR_CONTENT());
                        header2.createCell(4).setCellValue(weiXin.getKeyword());
                    }
                    bugNum = bugNum + list.size();
            header.createCell(5).setCellValue("��¼������: " + CountTotalRecordNum.getDataNumAll(serverTable) + "   �쳣����Ϊ:" + bugNum);
        } else if ("IR_SID".equals(column)) {
            List<SpecialWord> list = new ArrayList<>();
            /*header.createCell(0).setCellValue("IR_ID");
            header.createCell(1).setCellValue("���ӵ�ַ");
            header.createCell(2).setCellValue("��������");
            header.createCell(3).setCellValue("��վ����");
            header.createCell(4).setCellValue("Ƶ������");
            header.createCell(5).setCellValue("����");
            header.createCell(6).setCellValue("������");*/
            header.createCell(0).setCellValue("���");
            header.createCell(1).setCellValue("��λ����");
            header.createCell(2).setCellValue("Ӧ�÷���");
            header.createCell(3).setCellValue("Ӧ�ñ���");
            header.createCell(4).setCellValue("Ӧ������");
            header.createCell(5).setCellValue("���Ƶ�ַ");
            header.createCell(6).setCellValue("���ƹؼ���");
            header.createCell(7).setCellValue("����ʱ��");
            //������쳣������
            int q = 0;
            for (int j = 0; j < where.length; j++) {
                for (int i = 0; i < filePath.length; i++) {
                    list = csw.searchMessage(serverTable, where[j] + getSensitiveWords(filePath[i]) + "%)", false);
                    for (int z = 1; z < list.size() + 1; z++) {
                        q = q + 1;
                        SpecialWord specialWord = list.get(z - 1);
                        XSSFRow header2 = sheet.createRow(q);
                        /*header2.createCell(0).setCellValue(specialWord.getId());
                        header2.createCell(1).setCellValue(specialWord.getUrl());
                        header2.createCell(2).setCellValue(specialWord.getContent());
                        header2.createCell(3).setCellValue(specialWord.getSiteName());
                        header2.createCell(4).setCellValue(specialWord.getChannel());
                        header2.createCell(5).setCellValue(specialWord.getGroupname());
                        header2.createCell(6).setCellValue(specialWord.getKeyword());
                        header2.createCell(7).setCellValue(specialWord.getKeyword());*/
                        String siteName = specialWord.getSiteName();
                        System.out.println(siteName);
                        String code = "";
                        String unitName = siteName;
                        int index = siteName.indexOf("_");
                        if (index != -1) {
                            code = siteName.substring(0, index);
                            unitName = siteName.substring(index + 1);
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        Long time = new Date().getTime() - 1000 * 60 * 60 * 24;
                        String date = sdf.format(time);
                        header2.createCell(0).setCellValue(q);
                        header2.createCell(1).setCellValue(unitName);
                        header2.createCell(2).setCellValue(DBConnector.groupType);
                        header2.createCell(3).setCellValue(code);
                        header2.createCell(4).setCellValue(specialWord.getChannel());
                        header2.createCell(5).setCellValue(specialWord.getUrl());
                        header2.createCell(6).setCellValue(specialWord.getKeyword());
                        header2.createCell(7).setCellValue(date);
                    }
                    bugNum = bugNum + list.size();
                }
            }
            header.createCell(8).setCellValue("��¼������: " + CountTotalRecordNum.getDataNumAll(serverTable) + "   �쳣����Ϊ:" + bugNum);

        } else if ("FileName".equals(column)) {
            List<FIleModel> list = new ArrayList<>();
            header.createCell(0).setCellValue("�ļ���");
            header.createCell(1).setCellValue("�ļ���ַ");
            header.createCell(2).setCellValue("ip");
            header.createCell(3).setCellValue("������");
            //������쳣������
            int q = 0;

            for (int j = 0; j < where.length; j++) {
                for (int i = 0; i < filePath.length; i++) {
                    String str = where[j] + getSensitiveWords(filePath[i]) + "%)";
                    list = csw.searchMessage(serverTable, where[j] + getSensitiveWords(filePath[i]) + "%)", false);
                    //list = csw.searchMessage(serverTable,"WP_Content=(%"+getSensitiveWords(filePath[i])	+"%)",false);
                    for (int z = 1; z < list.size() + 1; z++) {
                        q = q + 1;
                        FIleModel fIleModel = list.get(z - 1);
                        XSSFRow header2 = sheet.createRow(q);
                        header2.createCell(0).setCellValue(fIleModel.getFilename());
                        header2.createCell(1).setCellValue(fIleModel.getFilepath());
                        header2.createCell(2).setCellValue(fIleModel.getIp());
                        header2.createCell(3).setCellValue(fIleModel.getKey());
                    }
                    bugNum = bugNum + list.size();
                }
            }
            header.createCell(4).setCellValue("��¼������: " + CountTotalRecordNum.getDataNumAll(serverTable) + "   �쳣����Ϊ:" + bugNum);

        }else if("IR_CREATED_AT".equals(column)) {//����΢���������
            List<WeiBuo> list=new LinkedList<>();
            header.createCell(0).setCellValue("IR_MID");
            header.createCell(1).setCellValue("IR_SCREEN_NAME");
            header.createCell(2).setCellValue("IR_SITENAME");
            header.createCell(3).setCellValue("url");
            header.createCell(4).setCellValue("����");
            header.createCell(5).setCellValue("������");
            //������쳣������
            int q=0;

//			for(int j=0; j<where.length; j++) {
//				for(int i=0; i<filePath.length; i++) {
//					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
//					System.out.println("where[j]"+where[j]);
            //list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%) and (IR_AUTHORS=%�й�����% or IR_AUTHORS=%�����ݺ�% or IR_AUTHORS=%����˼���% )",false);
            list = csw.searchMessage(serverTable,"IR_STATUS_CONTENT=(%"+getSensitiveWords(filePath[0])	+"%)",false);
            //list = csw.searchMessage(serverTable,"IR_SITENAME=(�����ݺ�,ͼ˵����) and IR_STATUS_CONTENT=%"+getSensitiveWords(filePath[i])+"%",false);
            for(int z=1;z<list.size()+1;z++) {
                q=q+1;
                WeiBuo weiBuo=list.get(z-1);
                XSSFRow header2=sheet.createRow(q);
                header2.createCell(0).setCellValue(weiBuo.getIR_MID());
                header2.createCell(1).setCellValue(weiBuo.getIR_SCREEN_NAME());
                header2.createCell(2).setCellValue(weiBuo.getIR_SITENAME());
                header2.createCell(3).setCellValue(weiBuo.getIR_URLNAME());
                header2.createCell(4).setCellValue(weiBuo.getIR_STATUS_CONTENT());
                header2.createCell(5).setCellValue(weiBuo.getKeyword());
            }
            bugNum=bugNum+list.size();
            System.out.println("");
//				}
//			}
            header.createCell(6).setCellValue("��¼������: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   �쳣����Ϊ:"+bugNum);

        }else if("id".equals(column)) {
            List<JCMS> list=new LinkedList<>();
            header.createCell(0).setCellValue("id");
            header.createCell(1).setCellValue("ip");
            header.createCell(2).setCellValue("���ݿ�����");
            header.createCell(3).setCellValue("������");
            header.createCell(4).setCellValue("�ı�");
            header.createCell(5).setCellValue("������");
            //������쳣������
            int q=0;
            for(int j=0; j<where.length; j++) {
                for(int i=0; i<filePath.length; i++) {
                    String str=where[j]+getSensitiveWords(filePath[i])+"%)";
                    list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);
                    for(int z=1;z<list.size()+1;z++) {
                        q=q+1;
                        JCMS jcms=list.get(z-1);
                        XSSFRow header2=sheet.createRow(q);
                        header2.createCell(0).setCellValue(jcms.getSID());
                        header2.createCell(1).setCellValue(jcms.getSIP());
                        header2.createCell(2).setCellValue(jcms.getDb_name());
                        header2.createCell(3).setCellValue(jcms.getTB_NAME());
                        header2.createCell(4).setCellValue(jcms.getTrscontent());
                        header2.createCell(5).setCellValue(jcms.getKeyword());
                    }
                    bugNum=bugNum+list.size();
                    System.out.println("");
                }
            }
            header.createCell(6).setCellValue("��¼������: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   �쳣����Ϊ:"+bugNum);
        }else if("Y_ID".equals(column)) {
            List<JCMS> list=new LinkedList<>();
            header.createCell(0).setCellValue("id");
            header.createCell(1).setCellValue("ip");
            header.createCell(2).setCellValue("���ݿ�����");
            header.createCell(3).setCellValue("������");
            header.createCell(4).setCellValue("�ı�");
            header.createCell(5).setCellValue("������");
            //������쳣������
            int q=0;
            for(int j=0; j<where.length; j++) {
                for(int i=0; i<filePath.length; i++) {
                    String str=where[j]+getSensitiveWords(filePath[i])+"%)";
                    list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);
                    for(int z=1;z<list.size()+1;z++) {
                        q=q+1;
                        JCMS jcms=list.get(z-1);
                        XSSFRow header2=sheet.createRow(q);
                        header2.createCell(0).setCellValue(jcms.getSID());
                        header2.createCell(1).setCellValue(jcms.getSIP());
                        header2.createCell(2).setCellValue(jcms.getDb_name());
                        header2.createCell(3).setCellValue(jcms.getTB_NAME());
                        header2.createCell(4).setCellValue(jcms.getTrscontent());
                        header2.createCell(5).setCellValue(jcms.getKeyword());
                    }
                    bugNum=bugNum+list.size();
                    System.out.println("");
                }
            }
            header.createCell(6).setCellValue("��¼������: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   �쳣����Ϊ:"+bugNum);
        }
        //�����еĿ��
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            sheet.setColumnWidth(i, 255 * 20);
        }
        header.setHeightInPoints(30);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(DBConnector.detailFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //��ָ���ļ�д������
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
//        main0(null);
    }

    /**
     * ����վͳ��
     *
     * @throws IOException
     */
    public static void countBySize() throws IOException {
        String serverTable = DBConnector.serverTable;
        String[] filePath = {DBConnector.biaodashi};
        //ʹ�����ݿ�ĵ������������ݿ�
        String column = ConcatSiteName.getColumnName(serverTable);
        String column3 = ConcatSiteName.getColumnName3(serverTable);
        //�������ݿ���ж�
        if ("IR_HKEY".equals(column)) {
            //�������дʵ�map����
            HashMap<String, Integer> hashMapSpecialAll = ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
            String columnname = "΢������";
            //�������
            CreateTable.create(serverTable, columnname, hashMapSpecialAll);
        }else if("IR_CREATED_AT".equals(column)) {
            //�������дʵ�map����
            HashMap<String,Integer> hashMapSpecialAll=ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
            String columnname="΢������";
            //�������
            CreateTable.create(serverTable, columnname, hashMapSpecialAll);
        } else if ("IR_SID".equals(column)) {            //����΢���������
            HashMap<String, Integer> hashMapSpecialAll = ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
            String columnname = "��վ����";
            //�������
            CreateTable.create(serverTable, columnname, hashMapSpecialAll);
        } else if ("FileName".equals(column)) {
            HashMap<String, Integer> hashMapSpecialAll = ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
            String columnname = "ip����";
            //�������
            CreateTable.create(serverTable, columnname, hashMapSpecialAll);
        } else if ("id".equals(column)) {
            HashMap<String, Integer> hashMapSpecialAll = ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
            String columnname = "������";
            //�������
            CreateTable.create(serverTable, columnname, hashMapSpecialAll);
        }else if ("Y_ID".equals(column)) {
            HashMap<String, Integer> hashMapSpecialAll = ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
            String columnname = "������";
            //�������
            CreateTable.create(serverTable, columnname, hashMapSpecialAll);
        }
    }
}
