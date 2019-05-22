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
import utils.model.SpecialWord;

public class ConcatSiteName {
    static DBConnector db = new DBConnector();
    static TRSConnection conn = null;
    static TRSResultSet rs = null;

    public static String[] getAllSiteNAme(String tableName) {
        String[] whereall = new String[]{};
        conn = db.getDBConnection();
        //所有的敏感信息列表
        String column = ConcatSiteName.getColumnName(tableName);
        String column3 = ConcatSiteName.getColumnName3(tableName);
        List list = null;
        rs = new TRSResultSet();
        String[] allsitename = null;
        System.out.println("column" + column);
        try {
            rs = conn.executeSelect(tableName, column + "!='-1'", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            int iClassNum = 0;
            if ("IR_HKEY".equals(column)) {
                iClassNum = rs.classResult("IR_WEIXINID", "", 10, "", false,
                        false);
            } else if ("IR_SID".equals(column)) {            //寻常新闻
                iClassNum = rs.classResult("IR_SITENAME", "", 10, "", false,
                        false);
            } else if ("IR_AUTHORS".equals(column)) {        //微信
                iClassNum = rs.classResult("IR_SITENAME", "", 10, "", false,
                        false);
            } else if ("IR_CREATED_AT".equals(column)) {        //微博
//                iClassNum = rs.classResult("IR_SITENAME", "", 10, "", false,
                iClassNum = rs.classResult("IR_SCREEN_NAME", "", 10, "", false,
                        false);
            } else if ("FILENAME".equals(column)) {        //网盘
                iClassNum = rs.classResult("IR_SITENAME", "", 10, "", false,
                        false);
            } else if ("Y_ID".equals(column)) {        //JCMS
                iClassNum = rs.classResult("SIP", "", 10, "", false,
                        false);
            } else if ("FileName".equals(column)) {
                iClassNum = rs.classResult("ip", "", 10, "", false,
                        false);
            } else if ("id".equals(column)) {
                iClassNum = rs.classResult("SIP", "", 10, "", false,
                        false);
            }


            System.out.println("ClassCount= " + rs.getClassCount());
            allsitename = new String[iClassNum];
            for (int i = 0; i < iClassNum; i++) {
                ClassInfo classInfo = rs.getClassInfo(i);
                String sitename = classInfo.strValue;
                if (sitename.indexOf("(") > 0) {
                    sitename = sitename.substring(0, sitename.indexOf("("));
                }
                if (sitename != null && sitename.contains("-----")) {
                    System.out.println("sitename" + sitename.length() + "sitename" + sitename.indexOf("-----"));
                    sitename = sitename.substring(0, sitename.indexOf("-----"));
                } else if (sitename != null && sitename.contains("/")) {
                    System.out.println("sitename" + sitename.length() + "sitename" + sitename.indexOf("/"));
                    sitename = sitename.substring(0, sitename.indexOf("/"));
                }
                if ("IR_HKEY".equals(column)) {//微信
                    allsitename[i]="(IR_WEIXINID=%"+sitename/*.replaceAll("-", "\\\\-")*/+"%) and (IR_CONTENT=%";
//                    allsitename[i] = "IR_AUTHORS=(海油发展安全环保公司四川办事处,海油珠海之家,海油发展美钻深水系统有限公司,海油微讯,海油微庭,安全海油信科,海油发展EAP,海油安环公开课,海油发展,工程技术公司,未蓝直播,中海油信息科技有限公司,销售服务公司,海油发展配餐服务公司,印象配餐,海油智选生活,配餐采购食堂,海油发展物流公司,中海油能源发展管道工程分公司,中海油（天津）管道工程技术有限公司,院知院味儿,PCEC,工业水处理,无机盐工业,中海油常州院,艾涂邦,国家涂料质量监督检验中心,涂料工业,粉末学会,中海铂业,海油发展上海地区中心,海油发展物流公司) AND (Boxiong,Caihou,Choronology,Dongliang,Hualin,Huanning,Jiemin,Jihua,Keping,Lixin,NuerBaikeli,Qingshan,Shulin,SuRong,Tianpu,Tienan,Tonghai,Weizhong,Wenjin,Xiaolin,Xilai,Xingguo,Xinhua,Xiyou,Xizhao,Xuezhong,YangJing,Yongchun,Yongkang,Yongyuan,Zhenfang,Zhengcai,艾宝俊,艾文礼,白恩培,白克力,白向群,白雪山,薄熙来,伯雄,才厚,蔡希有,曹白隽,曾志权,曾志权,常小兵,陈安众,陈柏槐,陈川平,陈传书,陈刚,陈国峰,陈树隆,陈铁新,陈同海,陈文金,陈旭,陈雪枫,陈质枫,成涛,仇和,传忠同志,戴春宁,邓崎琳,东生同志,栋梁同志,窦玉沛,杜克平,杜善学,恩培,房峰辉,冯新柱,盖如垠,高建军,高剑云,龚清概,谷春立,谷宜成,关德,郭伯雄,郭林,郭永祥,郭有明,韩先聪,韩学键,何家成,贺家铁,宏伟同志,鸿志同志,胡世辉,胡正明,华林同志,焕宁,黄保东,黄兴国,霍克,计划同志,季建业,季缃绮,冀文林,家才同志,贾岷岫,江栋,姜锡肇,蒋洁敏,洁敏,金道铭,景春华,军民融合,俊波,克平,孔令中,昆生,赖德荣,赖小民,乐大克,勒绥东,李昌军,李成云,李崇禧,李春城,李春华,李达球,李东生,李华林,李嘉,李建华,李立国,李量,李士祥,李文科,李新华,李贻煌,李云峰,李长友,李志玲,力军同志,立新同志,栗智,梁滨,廖少华,廖永远,林晓轩,令计划,令政策,刘北宪,刘军谊,刘君,刘强,刘善桥,刘铁男,刘新齐,刘长虹,刘志庚,卢恩光,卢子跃,鲁炜,陆武成,罗会文,罗伟中,罗智峰,吕锡文,马建,毛小兵,孟宏伟,孟伟,苗春生,苗永进,倪发科,聂宝栋,聂春玉,努尔·白克力,潘逸阳,蒲波,普世价值,齐平景,秦玉海,青山同志,邱大明,曲淑辉,任润厚,荣绍宏,三运,少春,申维辰,沈培平,时希平,树林同志,司献民,斯鑫良,宋林,苏宏章,苏荣,苏树林,隋凤富,孙波,孙鸿志,孙怀山,孙兆学,孙政才,所有制中立,谈红,谭力,谭栖伟,汤东宁,天普,铁男,同海同志,童名谦,万庆良,王保安,王尔智,王赣江,王和,王宏江,王金华,王克勤,王立军,王立新,王珉,王敏,王三运,王帅延,王素毅,王天普,王铁,王威,王晓光,王晓林,王岩岫,王艳辉,王阳,王银成,王永春,王云戈,王志富,维辰,伟中,魏传忠,魏宏,魏民洲,魏志刚,温青山,文金同志,文伟,吴天君,吴浈,吴振芳,武长顺,西方宪政民主,希有,奚晓明,锡肇,熙来,喜武,夏安然,夏崇源,夏兴华,项俊波,肖天,小兵同志,晓林同志,新华同志,新自由主义,兴国同志,熊启中,熊跃辉,胥革非,徐才厚,徐钢,徐建一,徐敏杰,许爱民,许杰,许前飞,许宗衡,薛峰,学仲,阳宝华,杨崇勇,杨东平,杨栋梁,杨刚,杨焕宁,杨家才,杨晶,杨琨,杨鲁豫,杨森林,杨绍丞,杨思涛,杨卫泽,杨锡怀,杨振超,姚刚,姚木根,姚中民,尹海林,永春同志,永康,永远同志,余刚,余远辉,虞海燕,育军,袁亚康,张宝胜,张根恒,张化为,张杰辉,张昆生,张乐斌,张力夫,张力军,张少春,张田欣,张文雄,张喜武,张阳,张育军,张越,张哲英,张智江,章潮鼎,赵黎平,赵少麟,赵正永,赵智勇,真才基,振芳,郑玉焯,政才,中央政法委书记周,中央政治局委员薄,中央政治局委员孙,钟世坚,重庆市委书记薄,重庆市委书记孙,周本顺,周春雨,周军,周来振,周学仲,周永康,周镇宏,朱明国,祝作利,宗新华,秦光荣)";
                } else if ("IR_SID".equals(column)) {            //寻常新闻
                    allsitename[i] = "(IR_SITENAME='%" + sitename + "%') and (IR_URLCONTENT=%";
                } else if ("IR_AUTHORS".equals(column)) {
                    allsitename[i] = "(IR_SITENAME=%" + sitename + "%) and (IR_URLCONTENT=%";
                } else if ("IR_CREATED_AT".equals(column)) {        //微博
                    allsitename[i] = "(IR_SCREEN_NAME=%" + sitename + "%) and (IR_STATUS_CONTENT=%";
//                    allsitename[i] = "(IR_SITENAME=%" + sitename + "%) and (IR_STATUS_CONTENT=%";
                } else if ("FILENAME".equals(column)) {        //网盘
                    allsitename[i] = "(IR_SITENAME=%" + sitename + "%) and (IR_URLCONTENT=%";
                } else if ("Y_ID".equals(column)) {        //JCMS
                    allsitename[i] = "(SIP=%" + sitename + "%) and (TRSCONTENT=%";
                } else if ("FileName".equals(column)) {        //File
                    allsitename[i] = "(ip=%" + sitename + "%) and (WP_Content=%";
                } else if ("id".equals(column)) {        //File
                    allsitename[i] = "(SIP=%" + sitename + "%) and (TRSCONTENT=%";
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
        long num = 0;
        conn = db.getDBConnection();
        TRSColProperty col = null;
        try {
            col = conn.getStatCol(tableName, 0);
        } catch (TRSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return col.strColumn;
    }

    //获取需要搜索的数据库第三个字段
    public static String getColumnName3(String tableName) {
        long num = 0;
        conn = db.getDBConnection();
        TRSColProperty col = null;
        try {
            col = conn.getStatCol(tableName, 2);
        } catch (TRSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return col.strColumn;
    }

    /**
     * 查找相关的sitename总量
     *
     * @param tableName
     * @return
     */
    public static HashMap getSpecialSiteName(String tableName, String tiaojian) {
        String[] whereall = new String[]{};
        conn = db.getDBConnection();
        //所有的敏感信息列表
        String column = ConcatSiteName.getColumnName(tableName);
        String column3 = ConcatSiteName.getColumnName3(tableName);
        List list = null;
        rs = new TRSResultSet();//IR_CREATED_AT
        int iClassNum = 0;
        try {
            if ("IR_SID".equals(column)) {
                rs = conn.executeSelect(tableName, "IR_URLCONTENT=%" + tiaojian + "%", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            } else if ("id".equals(column)) {
                rs = conn.executeSelect(tableName, "TRSCONTENT=%" + tiaojian + "%", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            } else if ("Y_ID".equals(column)) {
                rs = conn.executeSelect(tableName, "TRSCONTENT=%" + tiaojian + "%", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            } else if ("IR_HKEY".equals(column)) {
                rs = conn.executeSelect(tableName, "IR_CONTENT=%" + tiaojian + "%", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
                //rs = conn.executeSelect(tableName, "IR_CONTENT=%"+tiaojian+"% and (IR_SITENAME=%海油螺号% or IR_SITENAME=%图说海油% )" , "", "", "", 0, TRSConstant.TCE_OFFSET, false);
                //rs = conn.executeSelect(tableName, "IR_AUTHORS=(中海油常州院,艾涂邦,国家涂料质量监督检验中心,涂料工业,粉末学会,院知院味儿,PCEC,工业水处理,无机盐工业,海发新能源,安装风采,中海油（天津）管道工程技术有限公司,渤油航建,工技小博,海油智选生活,工技小博,中海油井控中心,海油发展安全环保公司,中海油安技服,安全应急培训中心,中海油节能环保,北京安全环保工程技术研究院) AND (周永康,永康,中央政法委书记周,薄熙来,熙来,重庆市市委书记薄,中央政治局委员薄,孙政才,政才,重庆市市委书记孙,中央政治局委员孙,令计划,计划同志,郭伯雄,伯雄,徐才厚,才厚,苏荣,政协苏荣,杨晶,刘铁男,刘局长,铁男,努尔·白克力,白克力,王晓林,晓林同志,张喜武,喜武,鲁炜,李东生,东生同志,孟宏伟,宏伟同志,杨栋梁,栋梁同志,杨焕宁,焕宁,项俊波,俊波,申维辰,维辰,陈刚,张力军,力军,姚刚,杨家才,家才同志,张育军,育军,张少春,少春,孙鸿志,鸿志局长,张昆生,昆生,魏传忠,传忠同志,蒋洁敏,洁敏,廖永远,永远同志,王永春,永春同志,李新华,新华同志,李华林,华林同志,王立新,立新同志,温青山,青山同志,苏树林,树林同志,王天普,天普,陈同海,同海,蔡希有,希有,杜克平,克平,吴振芳,振芳,罗伟中,伟中,周学仲,学仲,姜锡肇,锡肇,关德,关德副局长,陈文金,文金同志,王三运,三运,黄兴国,兴国同志,常小兵,小兵同志,白恩培,恩培)", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            } else if ("IR_CREATED_AT".equals(column)) {
                //rs = conn.executeSelect(tableName, "IR_STATUS_CONTENT=%"+tiaojian+"%", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
                rs = conn.executeSelect(tableName, "IR_STATUS_CONTENT=%" + tiaojian + "%", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            }

        } catch (TRSException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            if ("IR_SID".equals(column)) {
                iClassNum = rs.classResult("IR_SITENAME", "", 0, "", true, false);
            } else if ("id".equals(column)) {
                iClassNum = rs.classResult("TB_NAME", "", 0, "", true, false);
            } else if ("Y_ID".equals(column)) {
                iClassNum = rs.classResult("TB_NAME", "", 0, "", true, false);
            } else if ("IR_HKEY".equals(column)) {
                iClassNum = rs.classResult("IR_WEIXINID", "", 0, "", true, false);
            } else if ("IR_CREATED_AT".equals(column)) {
//                iClassNum = rs.classResult("IR_SITENAME", "", 0, "", true, false);
                iClassNum = rs.classResult("IR_SCREEN_NAME", "", 0, "", true, false);
            }
        } catch (TRSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("ClassCount= " + rs.getClassCount());
        HashMap hashMap = new HashMap();
        for (int i = 0; i < iClassNum; i++) {
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
     * @param tableName
     * @return
     */
    public static HashMap getAllSiteNameData(String tableName) {
        String[] whereall = new String[]{};
        conn = db.getDBConnection();
        //所有的敏感信息列表
        String column = ConcatSiteName.getColumnName(tableName);
        String column3 = ConcatSiteName.getColumnName3(tableName);
        List list = null;
        rs = new TRSResultSet();
        int iClassNum = 0;
        try {
            if ("IR_SID".equals(column)) {
                rs = conn.executeSelect(tableName, "IR_SID!= '-1'", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            } else if ("id".equals(column)) {
                rs = conn.executeSelect(tableName, "id!= '-1'", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            } else if ("Y_ID".equals(column)) {
                rs = conn.executeSelect(tableName, "Y_ID!= '-1'", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            } else if ("IR_HKEY".equals(column)) {
                rs = conn.executeSelect(tableName, "IR_HKEY!='-1'", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            } else if ("IR_CREATED_AT".equals(column)) {
                rs = conn.executeSelect(tableName, "IR_CREATED_AT!='-1'", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            }
        } catch (TRSException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            if ("IR_SID".equals(column)) {
                iClassNum = rs.classResult("IR_SITENAME", "", 0, "", true, false);
            } else if ("id".equals(column)) {
                iClassNum = rs.classResult("TB_NAME", "", 0, "", true, false);
            } else if ("Y_ID".equals(column)) {
                iClassNum = rs.classResult("TB_NAME", "", 0, "", true, false);
            } else if ("IR_HKEY".equals(column)) {
                iClassNum = rs.classResult("IR_WEIXINID", "", 0, "", true, false);
            } else if ("IR_CREATED_AT".equals(column)) {
//                iClassNum = rs.classResult("IR_SITENAME", "", 0, "", true, false);
                iClassNum = rs.classResult("IR_SCREEN_NAME", "", 0, "", true, false);
            }
        } catch (TRSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("ClassCount= " + rs.getClassCount());
        HashMap hashMap = new HashMap<>();
        for (int i = 0; i < iClassNum; i++) {
            ClassInfo classInfo;
            try {
                classInfo = rs.getClassInfo(i);
                hashMap.put(classInfo.strValue, classInfo.iRecordNum + "");
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
        System.out.println("aa" + getColumnName("GongXiang_201_0423"));
    }
}
