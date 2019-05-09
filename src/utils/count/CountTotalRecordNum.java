package utils.count;

import java.util.List;

import com.eprobiti.trs.TRSConnection;
import com.eprobiti.trs.TRSException;
import com.eprobiti.trs.TRSResultSet;

import utils.db.DBConnector;

public class CountTotalRecordNum {
    static DBConnector db = new DBConnector();
    static TRSConnection conn = null;
    static TRSResultSet rs = null;

    /*
     * 获取所有的记录数
     */
    public static String getDataNumAll(String tableName) {
        long num = 0;
        String column = ConcatSiteName.getColumnName(tableName);
        conn = db.getDBConnection();
        //所有的敏感信息列表
        List list = null;
        rs = new TRSResultSet();
        try {
            rs = conn.executeSelect(tableName, column + "!='-1'", false);
        } catch (TRSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        num = rs.getRecordCount();

        return num + "";
    }

    /**
     * 获取指网站的数量
     *
     * @param tableName
     * @return
     */
    public static String getColumnNum(String tableName, String sitename) {
        long num = 0;
        conn = db.getDBConnection();
        String column = ConcatSiteName.getColumnName(tableName);
        String column3 = ConcatSiteName.getColumnName3(tableName);
        //所有的敏感信息列表
        List list = null;
        rs = new TRSResultSet();
        try {
            if ("IR_HKEY".equals(column)) {        //JCMS
                rs = conn.executeSelect(tableName, "IR_WEIXINID=" + sitename, false);
            } else if ("IR_SID".equals(column)) {
                rs = conn.executeSelect(tableName, "IR_SITENAME=" + sitename, false);
            } else if ("FileName".equals(column)) {
                rs = conn.executeSelect(tableName, "ip=" + sitename, false);
            } else if ("Y_ID".equals(column)) {        //JCMS
                rs = conn.executeSelect(tableName, "SIP=" + sitename, false);
            }


        } catch (TRSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        num = rs.getRecordCount();

        return num + "";
    }

}
