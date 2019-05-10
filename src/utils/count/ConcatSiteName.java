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
        //���е�������Ϣ�б�
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
            }else if("IR_SID".equals(column)) {			//Ѱ������
                iClassNum = rs.classResult("IR_SITENAME", "", 10, "", false,
                        false);
            }else if("IR_AUTHORS".equals(column)) {		//΢��
                iClassNum = rs.classResult("IR_SITENAME", "", 10, "", false,
                        false);
            }else if("IR_CREATED_AT".equals(column)) {		//΢��
                iClassNum = rs.classResult("IR_SITENAME", "", 10, "", false,
                        false);
            }else if("FILENAME".equals(column)) {		//����
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
                if("IR_HKEY".equals(column)) {//΢��
                    allsitename[i]="(IR_WEIXINID=%"+sitename+"%) and (IR_CONTENT=%";
                }else if("IR_SID".equals(column)) {			//Ѱ������
                    allsitename[i]="(IR_SITENAME='%"+sitename+"%') and (IR_URLCONTENT=%";
                }else if("IR_AUTHORS".equals(column)) {
                    allsitename[i]="(IR_SITENAME=%"+sitename+"%) and (IR_URLCONTENT=%";
                }else if("IR_CREATED_AT".equals(column)) {		//΢��
                    allsitename[i]="(IR_SITENAME=%"+sitename+"%) and (IR_STATUS_CONTENT=%";
                }else if("FILENAME".equals(column)) {		//����
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
    //��ȡ��Ҫ���������ݿ��һ���ֶ�
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
    //��ȡ��Ҫ���������ݿ�������ֶ�
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
     * ������ص�sitename����
     * @param tableName
     * @return
     */
    public static HashMap getSpecialSiteName(String tableName,String tiaojian) {
        String[] whereall=new String[] {};
        conn=db.getDBConnection();
        //���е�������Ϣ�б�
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
                rs = conn.executeSelect(tableName, "IR_AUTHORS=(�����ۿͻ������ƶ�ƽ̨,������΢��ƽ̨,���͸����Ϻ���ѧ���޹�˾,���͸��������ֹ�˾,���͸������зֹ�˾,���͸��������ֹ�˾,��������ũ������ƽ̨,�к���ѧ����,���͸���,�׹�ũҵ,���͸��������ֹ�˾,�й�-�������������޹�˾,�а����ɸ�Ӫ����Ѷ,���ͻ�����Ƹ,������ί,���׿շ�,���׶��齨��,���ͻ���˽�ҳ����ֲ�,�˲ſ���,����˼��,������ί,��������,QHSE,�ִ��������ĵ�֧��) AND (���»�,ף����,�p����,������,�L����,�L�W��,�L܊,�L���,�����,������,��ѧ��,������,�ܾ�,�ܴ���,�ܱ�˳,������ί�����,������ί��Ǳ�,������,�������ξ�ίԱ��,�������ξ�ίԱ��,��������ί�����,����,֣����,��,��Ż�,�w����,������,������,������,����ƽ,����܊,���,��ϲ��,���ٴ�,����܊,������,�³���,���ǽ�,����Ӣ,��Խ,������,����,��ϲ��,������,������,���ٴ�,������,������,���ֱ�,������,�Žܻ�,�Ż�Ϊ,�Ÿ���,�ű�ʤ,Ԭ�ǿ�,����,����,�ݺ���,��Զ��,���,��Զͬ־,����,����ͬ־,������,Ҧ����,Ҧľ��,Ҧ��,Ҧ��,��˼��,�,����u,���,���,����,������,������,��˼��,����ة,��ɭ��,��³ԥ,����,�,��Ҳ�,�����,���,���,�ƽ,�����,������,ѧ��,Ѧ��,���ں�,��ǰ��,���,����,������,�콨һ,���,���u��,��ź�,����,��Ծ��,������,�˹�ͬ־,���������x,����������,�»�ͬ־,����ͬ־,С��ͬ־,Ф��,헿���,���,���˻�,�ĳ�Դ,�İ�Ȼ,ϲ��,����,����,������,ϣ��,������������,������������,�䳤˳,���L�,����,����,���,�����,�Ă�,��ΰ,�Ľ�ͬ־,����ɽ,����ɽ,κ�I��,κ־��,κ����,κ��,κ����,κ����,ΰ��,ά��,��־��,���Ƹ�,������,������,����,���޻�,�����,������,������,������,����,����,������,������,��˧��,�����\,������,����,����,������,����܊,������,������,����,���꽭,����,���ӽ�,������,������,�f�c��,������,ͯ��ǫ,ͬ��ͬ־,����,����,������,�T��,̷��ΰ,̷��,̸��,����������,�O���u,�O���I,�O��,������,����ѧ,�ﻳɽ,���־,�ﲨ,��︻,�K����,�K�s,������,����,�պ���,����,˹����,˾����,����ͬ־,ʱϣƽ,ʯ��,ʯ����,����ƽ,��S��,��ά��,�ٴ�,����,�s�ۺ�,���ۺ�,�����,�����,�����,��ɽͬ־,����,��ƽ��,�����rֵ,������ֵ,�Ѳ�,������,Ŭ��?�ׄw��,Ŭ�����׿���,����,������,������,�߷���,�����M,������,�紺��,��ΰ,�Ϻꂥ,�Ϻ�ΰ,ëС��,��,������,�_����,���Ƿ�,��ΰ��,�޻���,½���,����,³�,�R���S,¬��Ծ,¬����,���I��,���F��,��־��,������,������,������,������,��ǿ,����,������,������,������,��Ӌ��,��ƻ�,������,�����h,����Զ,���ٻ�,����,����,����ͬ־,����ͬ־,��־��,���,��녷�,���Ʒ�,���ݻ�,�����A,���»�,���Ŀ�,��ʿ��,����,������,���,���,���A��,���,��|��,���,�����,���,���,�����,����,�����,�����,���綫,�ִ��,��С��,������,����,����,��ƽ,������,����,�����ں�,������,�����,����,�Y����,������,�K�a��,������,����,����,����,�Ҳ�ͬ־,������,����,����ҵ,�ƻ�ͬ־,����,�S�d��,���˹�,�Ʊ���,����,����ͬ־,�S�ں�,������,������,��־ͬ־,��ΰͬ־,�ؼ���,�μҳ�,��ѧ��,���ȴ�,������,������,����,������,�P��,�ص�,���˳�,�ȴ���,�����,�߽���,�߽���,������,������,�����x,�����,����,����ѧ,�ńwƽ,�ſ�ƽ,����ͬ־,����ͬ־,������,������,������,����ͬ־,���,�ɝ�,����,��|��,��Ľ�,�ͬ��,ꐄ�,ꐴ�ƽ,���ʷ�,��ѩ��,����,���Ľ�,��ͬ��,������,����¡,�¹���,�¸�,�´���,�´�ƽ,�°ػ�,�°���,��С��,���I��,��־Ȩ,�ܰ���,��ϣ��,�ź�,����,������,����,��ѩɽ,�ׇ�Ⱥ,����Ⱥ,�׿���,�׶���,������,������,������,Zhengcai,Zhenfang,Yongyuan,Yongkang,Yongchun,Xuezhong,Xizhao,Xiyou,Xinhua,Xingguo,Xilai,Xiaolin,Wenjin,Weizhong,Tonghai,Tienan,Tianpu,Shulin,Rong,Qingshan,Nuer,Lixin,Keping,Jing,Jihua,Jiemin,Huanning,Hualin,Dongliang,Choronology,Caihou,Boxiong,Baikeli,������,��������ί��Ǳ�,��������ί�����,��Э����,���ֳ�,��־�ֳ�,�ص¸��ֳ�)" +
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
        //���е�������Ϣ�б�
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
        System.out.println("aa"+getColumnName("��Ϣ�Ƽ�_10_78_65_15"));
    }
}
