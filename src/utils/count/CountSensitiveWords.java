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
     * 统计包含敏感词记录的条数
     *
     * @param tableName
     * @param sensitiveWords
     * @param isContinue
     * @return 条数
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
                System.out.println("第" + i + "条记录");
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
                System.out.println("敏感词出现的次数：" + sameWordNums(searchWords));
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
     * 查找敏感词的相关信息的详细信息
     *
     * @param tableName
     * @param sensitiveWords
     * @param isContinue
     * @param
     * @return 详细信息
     * @throws IOException
     */
    public List searchMessage(String tableName, String sensitiveWords, boolean isContinue) throws IOException {
        long num = 0;
        conn = db.getDBConnection();
        //所有的疑似信息列表
        List list = null;
        try {
            rs = new TRSResultSet();
            rs = conn.executeSelect(tableName, sensitiveWords, isContinue);
            System.out.println(sensitiveWords);
            num = rs.getRecordCount();
            //循环添加疑似信息
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
                    System.out.println("敏感词出现的次数：" + sameWordNums(searchWords));
                    list.add(specialWord);
                    log += specialWord.toString() + "\r\n" + "\n" + "\n";
                }
            } else if ("IR_AUTHORS".equals(column)) {        //增加微信类型
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
            }else if("IR_CREATED_AT".equals(column)) {		//增加微博类型
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
                    System.out.println("敏感词出现的次数：" + sameWordNums(searchWords));
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
                    System.out.println("敏感词出现的次数：" + sameWordNums(searchWords));
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
     * 获取相应目录下文本中所有的敏感词
     *
     * @param filePath
     * @return 文本中所有的敏感词
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
     * 每条记录出现相同敏感词的次数
     *
     * @param searchWords
     * @return 相同敏感词记录
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
     * 获取指定数据库的敏感词信息
     */
    public void searchAllMessage() {
        CountSensitiveWords csw = new CountSensitiveWords();
        long recordNum = 0;
        long recordTotal = 0;
        String serverTable = "AS$6$10$1";
        String[] where = {"(IR_SITENAME=集团公司官网) and (IR_URLCONTENT=", "(IR_SITENAME=OA系统首页的三个新闻栏目) and (IR_URLCONTENT=", "(IR_SITENAME=%工程信息综合管理平台%) and (IR_URLCONTENT=", "(IR_SITENAME=oA系统新闻中心页面) and (IR_URLCONTENT="};
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
                //				System.out.println("以上为  " + serverTable + " 表的站点为" + where[j] + "在此文件的‘" + filePath[i] + "’敏感词范围内，共检索到 " + recordNum + "条记录");
                for (int z = 0; z < list.size(); z++) {
                    SpecialWord specialWord = list.get(z);
                    System.out.println("list的详细" + list.get(z).toString());
                }

            }
        }
    }

    /**
     * 这个主方法是为了生成疑似信息详细列表
     */
    public static void detail() throws IOException {
        CountSensitiveWords csw = new CountSensitiveWords();
        String serverTable = DBConnector.serverTable;
        String[] filePath = new String[]{DBConnector.biaodashi};
        String[] where = ConcatSiteName.getAllSiteNAme(serverTable);
        //使用数据库的的列来区分数据库
        String column = ConcatSiteName.getColumnName(serverTable);
        String column3 = ConcatSiteName.getColumnName3(serverTable);
        //增加数据库的判断
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("疑似信息列表");
        XSSFRow header = sheet.createRow(0);
        int bugNum = 0;
        if ("IR_HKEY".equals(column)) {
            List<WeiXin> list = new ArrayList<>();
            header.createCell(0).setCellValue("序号");
            header.createCell(1).setCellValue("微信id");
            header.createCell(2).setCellValue("作者名");
            header.createCell(3).setCellValue("链接地址");
//            header.createCell(3).setCellValue("文本内容");
            header.createCell(4).setCellValue("检索词");
            //定义出异常的数量
            int q = 0;
                    list = csw.searchMessage(serverTable, DBConnector.siteList+"IR_CONTENT=%"+getSensitiveWords(filePath[0])+"%", false);
//                    list = csw.searchMessage(serverTable, "IR_AUTHORS=(海油发展安全环保公司四川办事处,海油珠海之家,海油发展美钻深水系统有限公司,海油微讯,海油微庭,安全海油信科,海油发展EAP,海油安环公开课,海油发展,工程技术公司,未蓝直播,中海油信息科技有限公司,销售服务公司,海油发展配餐服务公司,印象配餐,海油智选生活,配餐采购食堂,海油发展物流公司,中海油能源发展管道工程分公司,中海油（天津）管道工程技术有限公司,院知院味儿,PCEC,工业水处理,无机盐工业,中海油常州院,艾涂邦,国家涂料质量监督检验中心,涂料工业,粉末学会,中海铂业,海油发展上海地区中心,海油发展物流公司) AND (Boxiong,Caihou,Choronology,Dongliang,Hualin,Huanning,Jiemin,Jihua,Keping,Lixin,NuerBaikeli,Qingshan,Shulin,SuRong,Tianpu,Tienan,Tonghai,Weizhong,Wenjin,Xiaolin,Xilai,Xingguo,Xinhua,Xiyou,Xizhao,Xuezhong,YangJing,Yongchun,Yongkang,Yongyuan,Zhenfang,Zhengcai,艾宝俊,艾文礼,白恩培,白克力,白向群,白雪山,薄熙来,伯雄,才厚,蔡希有,曹白隽,曾志权,曾志权,常小兵,陈安众,陈柏槐,陈川平,陈传书,陈刚,陈国峰,陈树隆,陈铁新,陈同海,陈文金,陈旭,陈雪枫,陈质枫,成涛,仇和,传忠同志,戴春宁,邓崎琳,东生同志,栋梁同志,窦玉沛,杜克平,杜善学,恩培,房峰辉,冯新柱,盖如垠,高建军,高剑云,龚清概,谷春立,谷宜成,关德,郭伯雄,郭林,郭永祥,郭有明,韩先聪,韩学键,何家成,贺家铁,宏伟同志,鸿志同志,胡世辉,胡正明,华林同志,焕宁,黄保东,黄兴国,霍克,计划同志,季建业,季缃绮,冀文林,家才同志,贾岷岫,江栋,姜锡肇,蒋洁敏,洁敏,金道铭,景春华,军民融合,俊波,克平,孔令中,昆生,赖德荣,赖小民,乐大克,勒绥东,李昌军,李成云,李崇禧,李春城,李春华,李达球,李东生,李华林,李嘉,李建华,李立国,李量,李士祥,李文科,李新华,李贻煌,李云峰,李长友,李志玲,力军同志,立新同志,栗智,梁滨,廖少华,廖永远,林晓轩,令计划,令政策,刘北宪,刘军谊,刘君,刘强,刘善桥,刘铁男,刘新齐,刘长虹,刘志庚,卢恩光,卢子跃,鲁炜,陆武成,罗会文,罗伟中,罗智峰,吕锡文,马建,毛小兵,孟宏伟,孟伟,苗春生,苗永进,倪发科,聂宝栋,聂春玉,努尔·白克力,潘逸阳,蒲波,普世价值,齐平景,秦玉海,青山同志,邱大明,曲淑辉,任润厚,荣绍宏,三运,少春,申维辰,沈培平,时希平,树林同志,司献民,斯鑫良,宋林,苏宏章,苏荣,苏树林,隋凤富,孙波,孙鸿志,孙怀山,孙兆学,孙政才,所有制中立,谈红,谭力,谭栖伟,汤东宁,天普,铁男,同海同志,童名谦,万庆良,王保安,王尔智,王赣江,王和,王宏江,王金华,王克勤,王立军,王立新,王珉,王敏,王三运,王帅延,王素毅,王天普,王铁,王威,王晓光,王晓林,王岩岫,王艳辉,王阳,王银成,王永春,王云戈,王志富,维辰,伟中,魏传忠,魏宏,魏民洲,魏志刚,温青山,文金同志,文伟,吴天君,吴浈,吴振芳,武长顺,西方宪政民主,希有,奚晓明,锡肇,熙来,喜武,夏安然,夏崇源,夏兴华,项俊波,肖天,小兵同志,晓林同志,新华同志,新自由主义,兴国同志,熊启中,熊跃辉,胥革非,徐才厚,徐钢,徐建一,徐敏杰,许爱民,许杰,许前飞,许宗衡,薛峰,学仲,阳宝华,杨崇勇,杨东平,杨栋梁,杨刚,杨焕宁,杨家才,杨晶,杨琨,杨鲁豫,杨森林,杨绍丞,杨思涛,杨卫泽,杨锡怀,杨振超,姚刚,姚木根,姚中民,尹海林,永春同志,永康,永远同志,余刚,余远辉,虞海燕,育军,袁亚康,张宝胜,张根恒,张化为,张杰辉,张昆生,张乐斌,张力夫,张力军,张少春,张田欣,张文雄,张喜武,张阳,张育军,张越,张哲英,张智江,章潮鼎,赵黎平,赵少麟,赵正永,赵智勇,真才基,振芳,郑玉焯,政才,中央政法委书记周,中央政治局委员薄,中央政治局委员孙,钟世坚,重庆市委书记薄,重庆市委书记孙,周本顺,周春雨,周军,周来振,周学仲,周永康,周镇宏,朱明国,祝作利,宗新华,秦光荣)", false);
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
            header.createCell(5).setCellValue("记录数总量: " + CountTotalRecordNum.getDataNumAll(serverTable) + "   异常数量为:" + bugNum);
        } else if ("IR_SID".equals(column)) {
            List<SpecialWord> list = new ArrayList<>();
            /*header.createCell(0).setCellValue("IR_ID");
            header.createCell(1).setCellValue("链接地址");
            header.createCell(2).setCellValue("链接内容");
            header.createCell(3).setCellValue("网站名称");
            header.createCell(4).setCellValue("频道名称");
            header.createCell(5).setCellValue("组名");
            header.createCell(6).setCellValue("检索词");*/
            header.createCell(0).setCellValue("序号");
            header.createCell(1).setCellValue("单位名称");
            header.createCell(2).setCellValue("应用分类");
            header.createCell(3).setCellValue("应用编码");
            header.createCell(4).setCellValue("应用名称");
            header.createCell(5).setCellValue("疑似地址");
            header.createCell(6).setCellValue("疑似关键词");
            header.createCell(7).setCellValue("发现时间");
            //定义出异常的数量
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
            header.createCell(8).setCellValue("记录数总量: " + CountTotalRecordNum.getDataNumAll(serverTable) + "   异常数量为:" + bugNum);

        } else if ("FileName".equals(column)) {
            List<FIleModel> list = new ArrayList<>();
            header.createCell(0).setCellValue("文件名");
            header.createCell(1).setCellValue("文件地址");
            header.createCell(2).setCellValue("ip");
            header.createCell(3).setCellValue("检索词");
            //定义出异常的数量
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
            header.createCell(4).setCellValue("记录数总量: " + CountTotalRecordNum.getDataNumAll(serverTable) + "   异常数量为:" + bugNum);

        }else if("IR_CREATED_AT".equals(column)) {//增加微博库的类型
            List<WeiBuo> list=new LinkedList<>();
            header.createCell(0).setCellValue("IR_MID");
            header.createCell(1).setCellValue("IR_SCREEN_NAME");
            header.createCell(2).setCellValue("IR_SITENAME");
            header.createCell(3).setCellValue("url");
            header.createCell(4).setCellValue("内容");
            header.createCell(5).setCellValue("检索词");
            //定义出异常的数量
            int q=0;

//			for(int j=0; j<where.length; j++) {
//				for(int i=0; i<filePath.length; i++) {
//					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
//					System.out.println("where[j]"+where[j]);
            //list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%) and (IR_AUTHORS=%中国海油% or IR_AUTHORS=%海油螺号% or IR_AUTHORS=%海油思享汇% )",false);
            list = csw.searchMessage(serverTable,"IR_STATUS_CONTENT=(%"+getSensitiveWords(filePath[0])	+"%)",false);
            //list = csw.searchMessage(serverTable,"IR_SITENAME=(海油螺号,图说海油) and IR_STATUS_CONTENT=%"+getSensitiveWords(filePath[i])+"%",false);
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
            header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);

        }else if("id".equals(column)) {
            List<JCMS> list=new LinkedList<>();
            header.createCell(0).setCellValue("id");
            header.createCell(1).setCellValue("ip");
            header.createCell(2).setCellValue("数据库名称");
            header.createCell(3).setCellValue("表名称");
            header.createCell(4).setCellValue("文本");
            header.createCell(5).setCellValue("检索词");
            //定义出异常的数量
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
            header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
        }else if("Y_ID".equals(column)) {
            List<JCMS> list=new LinkedList<>();
            header.createCell(0).setCellValue("id");
            header.createCell(1).setCellValue("ip");
            header.createCell(2).setCellValue("数据库名称");
            header.createCell(3).setCellValue("表名称");
            header.createCell(4).setCellValue("文本");
            header.createCell(5).setCellValue("检索词");
            //定义出异常的数量
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
            header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
        }
        //设置列的宽度
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
//        main0(null);
    }

    /**
     * 分网站统计
     *
     * @throws IOException
     */
    public static void countBySize() throws IOException {
        String serverTable = DBConnector.serverTable;
        String[] filePath = {DBConnector.biaodashi};
        //使用数据库的的列来区分数据库
        String column = ConcatSiteName.getColumnName(serverTable);
        String column3 = ConcatSiteName.getColumnName3(serverTable);
        //增加数据库的判断
        if ("IR_HKEY".equals(column)) {
            //增加敏感词的map集合
            HashMap<String, Integer> hashMapSpecialAll = ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
            String columnname = "微信名称";
            //创建表格
            CreateTable.create(serverTable, columnname, hashMapSpecialAll);
        }else if("IR_CREATED_AT".equals(column)) {
            //增加敏感词的map集合
            HashMap<String,Integer> hashMapSpecialAll=ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
            String columnname="微博名称";
            //创建表格
            CreateTable.create(serverTable, columnname, hashMapSpecialAll);
        } else if ("IR_SID".equals(column)) {            //增加微博库的类型
            HashMap<String, Integer> hashMapSpecialAll = ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
            String columnname = "网站名称";
            //创建表格
            CreateTable.create(serverTable, columnname, hashMapSpecialAll);
        } else if ("FileName".equals(column)) {
            HashMap<String, Integer> hashMapSpecialAll = ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
            String columnname = "ip名称";
            //创建表格
            CreateTable.create(serverTable, columnname, hashMapSpecialAll);
        } else if ("id".equals(column)) {
            HashMap<String, Integer> hashMapSpecialAll = ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
            String columnname = "表名称";
            //创建表格
            CreateTable.create(serverTable, columnname, hashMapSpecialAll);
        }else if ("Y_ID".equals(column)) {
            HashMap<String, Integer> hashMapSpecialAll = ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
            String columnname = "表名称";
            //创建表格
            CreateTable.create(serverTable, columnname, hashMapSpecialAll);
        }
    }
}
