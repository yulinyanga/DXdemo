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
                rs = conn.executeSelect(tableName, "IR_CONTENT=%"+tiaojian+"%", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
                //rs = conn.executeSelect(tableName, "IR_CONTENT=%"+tiaojian+"% and (IR_SITENAME=%海油螺号% or IR_SITENAME=%图说海油% )" , "", "", "", 0, TRSConstant.TCE_OFFSET, false);
                //rs = conn.executeSelect(tableName, "IR_AUTHORS=(中海油常州院,艾涂邦,国家涂料质量监督检验中心,涂料工业,粉末学会,院知院味儿,PCEC,工业水处理,无机盐工业,海发新能源,安装风采,中海油（天津）管道工程技术有限公司,渤油航建,工技小博,海油智选生活,工技小博,中海油井控中心,海油发展安全环保公司,中海油安技服,安全应急培训中心,中海油节能环保,北京安全环保工程技术研究院) AND (周永康,永康,中央政法委书记周,薄熙来,熙来,重庆市市委书记薄,中央政治局委员薄,孙政才,政才,重庆市市委书记孙,中央政治局委员孙,令计划,计划同志,郭伯雄,伯雄,徐才厚,才厚,苏荣,政协苏荣,杨晶,刘铁男,刘局长,铁男,努尔·白克力,白克力,王晓林,晓林同志,张喜武,喜武,鲁炜,李东生,东生同志,孟宏伟,宏伟同志,杨栋梁,栋梁同志,杨焕宁,焕宁,项俊波,俊波,申维辰,维辰,陈刚,张力军,力军,姚刚,杨家才,家才同志,张育军,育军,张少春,少春,孙鸿志,鸿志局长,张昆生,昆生,魏传忠,传忠同志,蒋洁敏,洁敏,廖永远,永远同志,王永春,永春同志,李新华,新华同志,李华林,华林同志,王立新,立新同志,温青山,青山同志,苏树林,树林同志,王天普,天普,陈同海,同海,蔡希有,希有,杜克平,克平,吴振芳,振芳,罗伟中,伟中,周学仲,学仲,姜锡肇,锡肇,关德,关德副局长,陈文金,文金同志,王三运,三运,黄兴国,兴国同志,常小兵,小兵同志,白恩培,恩培)", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
            }else if("IR_CREATED_AT".equals(column)) {
                //rs = conn.executeSelect(tableName, "IR_STATUS_CONTENT=%"+tiaojian+"%", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
                rs = conn.executeSelect(tableName, "IR_STATUS_CONTENT=%"+tiaojian+"% and (IR_AUTHORS=%中国海油% or IR_AUTHORS=%海油螺号% or IR_AUTHORS=%海油思享汇% )", "", "", "", 0, TRSConstant.TCE_OFFSET, false);
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
        System.out.println("aa"+getColumnName("GongXiang_201_0423"));
    }
}
