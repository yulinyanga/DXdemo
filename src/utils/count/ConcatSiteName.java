package utils.count;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.eprobiti.trs.ClassInfo;
import com.eprobiti.trs.TRSColProperty;
import com.eprobiti.trs.TRSConnection;
import com.eprobiti.trs.TRSException;
import com.eprobiti.trs.TRSResultSet;
import com.trs.client.TRSConstant;

import utils.db.DBConnector;

public class ConcatSiteName {
    static DBConnector db = new DBConnector();
    static TRSConnection conn = null;
    static TRSResultSet rs = null;
    public static String[] getAllSiteNAme(String tableName) {
        String[] whereall=new String[] {};
        conn=db.getDBConnection();
        //所有的敏感信息列表
        String column=ConcatSiteName.getColumnName(tableName);
        String column3=ConcatSiteName.getColumnName3(tableName);
        List list=null;
        rs = new TRSResultSet();
        String [] allsitename=null;
        System.out.println("column"+column);
        try {
            rs = conn.executeSelect(tableName, column+"!='-1'", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            int iClassNum=0;
            if("IR_HKEY".equals(column)) {
                iClassNum = rs.classResult("IR_WEIXINID", "", 10, "", false,
                        false);
            }else if("IR_SID".equals(column)) {			//寻常新闻
                iClassNum = rs.classResult("IR_SITENAME", "", 10, "", false,
                        false);
            }else if("IR_AUTHORS".equals(column)) {		//微信
                iClassNum = rs.classResult("IR_SITENAME", "", 10, "", false,
                        false);
            }else if("IR_CREATED_AT".equals(column)) {		//微博
                iClassNum = rs.classResult("IR_SITENAME", "", 10, "", false,
                        false);
            }else if("FILENAME".equals(column)) {		//网盘
                iClassNum = rs.classResult("IR_SITENAME", "", 10, "", false,
                        false);
            }else if("Y_ID".equals(column)) {		//JCMS
                iClassNum = rs.classResult("SIP", "", 10, "", false,
                        false);
            }else if("FileName".equals(column)) {
                iClassNum = rs.classResult("ip", "", 10, "", false,
                        false);
            }else if("id".equals(column)) {
                iClassNum = rs.classResult("SIP", "", 10, "", false,
                        false);
            }


            System.out.println("ClassCount= " + rs.getClassCount());
            allsitename=new String[iClassNum];
            for (int i= 0; i< iClassNum; i++)
            {
                ClassInfo classInfo= rs.getClassInfo(i);
                String sitename=classInfo.strValue;
                if(sitename.indexOf("(")>0) {
                    sitename=sitename.substring(0,sitename.indexOf("("));
                }
                if(sitename!=null&&sitename.contains("-----")) {
                    System.out.println("sitename"+sitename.length()+"sitename"+sitename.indexOf("-----"));
                    sitename=sitename.substring(0, sitename.indexOf("-----"));
                }else if(sitename!=null&&sitename.contains("/")) {
                    System.out.println("sitename"+sitename.length()+"sitename"+sitename.indexOf("/"));
                    sitename=sitename.substring(0, sitename.indexOf("/"));
                }
                if("IR_HKEY".equals(column)) {//微信
                    allsitename[i]="(IR_WEIXINID=%"+sitename+"%) and (IR_CONTENT=%";
                }else if("IR_SID".equals(column)) {			//寻常新闻
                    allsitename[i]="(IR_SITENAME='%"+sitename+"%') and (IR_URLCONTENT=%";
                }else if("IR_AUTHORS".equals(column)) {
                    allsitename[i]="(IR_SITENAME=%"+sitename+"%) and (IR_URLCONTENT=%";
                }else if("IR_CREATED_AT".equals(column)) {		//微博
                    allsitename[i]="(IR_SITENAME=%"+sitename+"%) and (IR_STATUS_CONTENT=%";
                }else if("FILENAME".equals(column)) {		//网盘
                    allsitename[i]="(IR_SITENAME=%"+sitename+"%) and (IR_URLCONTENT=%";
                }else if("Y_ID".equals(column)) {		//JCMS
                    allsitename[i]="(SIP=%"+sitename+"%) and (TRSCONTENT=%";
                }else if("FileName".equals(column)) {		//File
                    allsitename[i]="(ip=%"+sitename+"%) and (WP_Content=%";
                }else if("id".equals(column)) {		//File
                    allsitename[i]="(SIP=%"+sitename+"%) and (TRSCONTENT=%";
                }


            }
        } catch (TRSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println("allsitename"+allsitename.length);

        return allsitename;
    }
    //获取需要搜索的数据库第一个字段
    public static String getColumnName(String tableName) {
        long num=0;
        conn=db.getDBConnection();
        TRSColProperty col = null;
        try {
            col = conn.getStatCol(tableName,0);
        } catch (TRSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return col.strColumn;
    }
    //获取需要搜索的数据库第三个字段
    public static String getColumnName3(String tableName) {
        long num=0;
        conn=db.getDBConnection();
        TRSColProperty col = null;
        try {
            col = conn.getStatCol(tableName,2);
        } catch (TRSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return col.strColumn;
    }
    /**
     * 查找相关的sitename总量
     * @param tableName
     * @return
     */
    public static HashMap getSpecialSiteName(String tableName,String tiaojian) {
        String[] whereall=new String[] {};
        conn=db.getDBConnection();
        //所有的敏感信息列表
        String column=ConcatSiteName.getColumnName(tableName);
        String column3=ConcatSiteName.getColumnName3(tableName);
        List list=null;
        rs = new TRSResultSet();//IR_CREATED_AT
        int iClassNum=0;
        try {
            if("IR_SID".equals(column)) {
                rs = conn.executeSelect(tableName, "IR_URLCONTENT=%"+tiaojian+"%", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            }else if("id".equals(column)) {
                rs = conn.executeSelect(tableName, "TRSCONTENT=%"+tiaojian+"%", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            }else if("IR_HKEY".equals(column)) {
                rs = conn.executeSelect(tableName, "IR_AUTHORS=(八所港客户服务移动平台,八所港微信平台,海油富岛上海化学有限公司,海油富岛华北分公司,海油富岛华中分公司,海油富岛东北分公司,广西富岛农化服务平台,中海化学党建,海油富岛,白宫农业,海油富岛西北分公司,中国-阿拉伯化肥有限公司,中阿撒可富营销资讯,海油华鹤招聘,华鹤团委,华鹤空分,华鹤队伍建设,海油华鹤私家车俱乐部,人才开发,华鹤思政,华鹤团委,廉政教育,QHSE,仓储物流中心党支部) AND (宗新华,祝作利,硃明國,朱明国,週永康,週學仲,週軍,週本順,周镇宏,周永康,周学仲,周来振,周军,周春雨,周本顺,重庆市委书记孙,重庆市委书记薄,钟世坚,中央政治局委员孙,中央政治局委员薄,中央政法委书记周,政才,郑玉焯,振芳,真才基,趙正永,赵智勇,赵正永,赵少麟,赵黎平,張育軍,張陽,張喜武,張少春,張力軍,張崑生,章潮鼎,张智江,张哲英,张越,张育军,张阳,张喜武,张文雄,张田欣,张少春,张力军,张力夫,张乐斌,张昆生,张杰辉,张化为,张根恒,张宝胜,袁亚康,育军,玉沛,虞海燕,余远辉,余刚,永远同志,永康,永春同志,尹海林,姚中民,姚木根,姚剛,姚刚,楊思濤,楊晶,楊傢纔,楊煥寧,楊棟樑,杨振超,杨锡怀,杨卫泽,杨思涛,杨绍丞,杨森林,杨鲁豫,杨琨,杨晶,杨家才,杨焕宁,杨刚,杨栋梁,杨东平,杨崇勇,阳宝华,学仲,薛峰,许宗衡,许前飞,许杰,许爱民,徐敏杰,徐建一,徐钢,徐纔厚,徐才厚,胥革非,熊跃辉,熊启中,兴国同志,新自由主義,新自由主义,新华同志,晓林同志,小兵同志,肖天,項俊波,项俊波,夏兴华,夏崇源,夏安然,喜武,熙来,锡肇,奚晓明,希有,西方憲政民主,西方宪政民主,武长顺,武長順,吳振芳,吴振芳,吴浈,吴天君,文偉,文伟,文金同志,溫青山,温青山,魏誌剛,魏志刚,魏民洲,魏宏,魏傳忠,魏传忠,伟中,维辰,王志富,王云戈,王永春,王银成,王阳,王艳辉,王岩岫,王曉林,王晓林,王晓光,王威,王铁,王天普,王素毅,王帅延,王叁運,王三运,王敏,王珉,王立新,王立軍,王立军,王克勤,王金华,王宏江,王和,王赣江,王尔智,王保安,萬慶良,万庆良,童名谦,同海同志,铁男,天普,汤东宁,譚力,谭栖伟,谭力,谈红,所有制中立,孫政纔,孫鴻誌,孫波,孙政才,孙兆学,孙怀山,孙鸿志,孙波,隋凤富,蘇樹林,蘇榮,苏树林,苏荣,苏宏章,宋林,斯鑫良,司献民,树林同志,时希平,石磊,石浩宇,沈培平,申維辰,申维辰,少春,三运,榮邵宏,荣邵宏,任润厚,曲淑辉,邱大明,青山同志,秦玉海,齐平景,普世價值,普世价值,蒲波,潘逸阳,努爾?白剋力,努尔·白克力,聶寶棟,聂春玉,聂宝栋,倪发科,苗永進,苗永进,苗春生,孟伟,孟宏偉,孟宏伟,毛小兵,马建,吕锡文,羅偉中,罗智峰,罗伟中,罗会文,陆武成,魯煒,鲁炜,盧子躍,卢子跃,卢恩光,劉誌庚,劉鐵男,刘志庚,刘长虹,刘新齐,刘铁男,刘善桥,刘强,刘君,刘军谊,刘北宪,令政策,令計劃,令计划,林晓轩,廖永遠,廖永远,廖少华,梁滨,栗智,立新同志,力军同志,李志玲,李长友,李雲峰,李云峰,李贻煌,李新華,李新华,李文科,李士祥,李量,李立国,李建华,李嘉,李華林,李华林,李東生,李东生,李达球,李春华,李春城,李崇禧,李成雲,李成云,李昌军,勒绥东,乐大克,赖小民,赖德荣,昆生,孔令,克平,康日新,俊波,军民融合,景春华,金道铭,洁敏,蔣潔敏,蒋洁敏,薑錫肇,姜锡肇,江棟,江栋,贾岷岫,家才同志,冀文林,季缃绮,季建业,计划同志,霍克,黃興國,黄兴国,黄保东,焕宁,华林同志,許宗衡,胡正明,胡世辉,鸿志同志,宏伟同志,贺家铁,何家成,韩学键,韩先聪,郭有明,郭永祥,郭林,郭伯雄,關德,关德,谷宜成,谷春立,龚清概,高剑云,高建军,盖如垠,冯新柱,房峰輝,房峰辉,恩培,杜善学,杜剋平,杜克平,栋梁同志,东生同志,鄧崎琳,邓崎琳,戴春宁,传忠同志,仇和,成濤,成涛,陳質楓,陳文金,陳同海,陳剛,陳川平,陈质枫,陈雪枫,陈旭,陈文金,陈同海,陈铁新,陈树隆,陈国峰,陈刚,陈传书,陈川平,陈柏槐,陈安众,常小兵,曾誌權,曾志权,曹白隽,蔡希有,才厚,伯雄,薄熙来,白云,白雪山,白嚮群,白向群,白克力,白恩培,艾文礼,艾寶俊,艾宝俊,Zhengcai,Zhenfang,Yongyuan,Yongkang,Yongchun,Xuezhong,Xizhao,Xiyou,Xinhua,Xingguo,Xilai,Xiaolin,Wenjin,Weizhong,Tonghai,Tienan,Tianpu,Shulin,Rong,Qingshan,Nuer,Lixin,Keping,Jing,Jihua,Jiemin,Huanning,Hualin,Dongliang,Choronology,Caihou,Boxiong,Baikeli,薄熙來,重庆市市委书记薄,重庆市市委书记孙,政协苏荣,刘局长,鸿志局长,关德副局长)" +
                        "", "", "", "", 0, TRSConstant.TCE_OFFSET, false);


            }else if("IR_CREATED_AT".equals(column)) {
                rs = conn.executeSelect(tableName, "IR_STATUS_CONTENT=%"+tiaojian+"%", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            }

        } catch (TRSException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            if("IR_SID".equals(column)) {
                iClassNum = rs.classResult("IR_SITENAME", "", 0, "", true,false);
            }else if("id".equals(column)) {
                iClassNum = rs.classResult("TB_NAME", "", 0, "", true,false);
            }else if("IR_HKEY".equals(column)) {
                iClassNum = rs.classResult("IR_WEIXINID", "", 0, "", true,false);
            }else if("IR_CREATED_AT".equals(column)) {
                iClassNum = rs.classResult("IR_SITENAME", "", 0, "", true,false);
            }
        } catch (TRSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("ClassCount= " + rs.getClassCount());
        HashMap  hashMap=new HashMap();
        for (int i= 0; i< iClassNum; i++)
        {
            ClassInfo classInfo;
            try {
                classInfo = rs.getClassInfo(i);
                hashMap.put(classInfo.strValue, classInfo.iRecordNum);
                System.out.println(classInfo.strValue + ": " +
                        classInfo.iValidNum + "/" +
                        classInfo.iRecordNum);
            } catch (TRSException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return hashMap;
    }
    /**
     *
     * @param tableName
     * @return
     */
    public static HashMap getAllSiteNameData(String tableName) {
        String[] whereall=new String[] {};
        conn=db.getDBConnection();
        //所有的敏感信息列表
        String column=ConcatSiteName.getColumnName(tableName);
        String column3=ConcatSiteName.getColumnName3(tableName);
        List list=null;
        rs = new TRSResultSet();
        int iClassNum=0;
        try {
            if("IR_SID".equals(column)) {
                rs = conn.executeSelect(tableName, "IR_SID!= '-1'", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            }else if("id".equals(column)) {
                rs = conn.executeSelect(tableName, "id!= '-1'", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            }else if("IR_HKEY".equals(column)) {
                rs = conn.executeSelect(tableName, "IR_HKEY!='-1'", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            }else if("IR_CREATED_AT".equals(column)) {
                rs = conn.executeSelect(tableName,  "IR_CREATED_AT!='-1'", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            }
        } catch (TRSException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            if("IR_SID".equals(column)) {
                iClassNum = rs.classResult("IR_SITENAME", "", 0, "", true,false);
            }else if("id".equals(column)) {
                iClassNum = rs.classResult("TB_NAME", "", 0, "", true,false);
            }else if("IR_HKEY".equals(column)) {
                iClassNum = rs.classResult("IR_WEIXINID", "", 0, "", true,false);
            }else if("IR_CREATED_AT".equals(column)) {
                iClassNum = rs.classResult("IR_SITENAME", "", 0, "", true,false);
            }
        } catch (TRSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("ClassCount= " + rs.getClassCount());
        HashMap  hashMap=new HashMap<>();
        for (int i= 0; i< iClassNum; i++)
        {
            ClassInfo classInfo;
            try {
                classInfo = rs.getClassInfo(i);
                hashMap.put(classInfo.strValue, classInfo.iRecordNum+"");
                System.out.println(classInfo.strValue + ": " +
                        classInfo.iValidNum + "/" +
                        classInfo.iRecordNum);
            } catch (TRSException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return hashMap;
    }
    public static void main(String[] args) {
        System.out.println("aa"+getColumnName("信息科技_10_78_65_15"));
    }
}
