package utils.db;

import com.eprobiti.trs.TRSConnection;
import com.eprobiti.trs.TRSException;
import com.eprobiti.trs.TRSResultSet;

import java.awt.*;
import java.util.Date;

/**
 * 数据库连接
 *
 * @author Hermes
 */
public class DBConnector {
    //todo 在导出前更新 ip 库名 数据库类型 表达式 存放路径(desktop、countFile、detailFile、logPath)
//    private final static String DB_URL = "localhost";	  //TRSServer数据库URL
    //private final static String DB_URL = "10.72.77.90";	  //TRSServer数据库URL
    //private final static String DB_URL = "10.72.76.187";	  //TRSServer数据库URL
//    private final static String DB_URL = "10.72.76.73";      //TRSServer数据库URL
    	private final static String DB_URL = "10.72.76.89";	  //TRSServer数据库URL
    private final static String DB_PORT = "8888";               //TRSServer端口
    private final static String DB_USERNAME = "system";            //TRSServer用户名
    private final static String DB_PASSWORD = "manager";         //TRSServer密码
    public final static String serverTable = "AS$3$E$1";        //TRSServer数据库名
    public final static String groupType = "外部网站";            //数据库类型  分为  外部网站、内部网站、微博、微信
    public final static String desktop = "C:\\Users\\11633\\Desktop\\";
//        public final static String biaodashi = desktop + "信息报表\\0507内网表达式.txt";        //内网表达式
    public final static String biaodashi = desktop + "信息报表\\表达式外网0508.txt";        //外网表达式
    public final static String countFile = desktop + "信息报表\\疑似信息\\" + groupType + "_疑似信息统计_" + (new Date().getMonth() + 1) + "月" + (new Date().getDate()) + "日" + (new Date()).getHours() + "时_.xlsx";        //存放疑似信息统计
    public final static String detailFile = desktop + "信息报表\\疑似信息\\" + groupType + "_疑似信息详细_" + (new Date().getMonth() + 1) + "月" + (new Date().getDate()) + "日" + (new Date()).getHours() + "时_.xlsx";        //存放疑似信息详细
    public final static String logPath = desktop + "信息报表\\log.txt";        //存放疑似信息详细

    /**
     * 获取数据库连接
     *
     * @return
     */
    public TRSConnection getDBConnection() {
        TRSConnection conn = null;
        try {
            conn = new TRSConnection();
            conn.connect(DB_URL, DB_PORT, DB_USERNAME, DB_PASSWORD, serverTable);
            //int iBackupNum = conn.backupDatabases("demo3", "c:\\bak\\demo3.bak");
        } catch (TRSException e) {
            System.out.println("ErrorCode: " + e.getErrorCode());
            System.out.println("ErrorString: " + e.getErrorString());
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param
     */
    public void closeConnection(TRSConnection conn) {
        try {
            if (conn != null) {
                if (!conn.isClosed()) {   //判断当前连接连接对象如果没有被关闭就调用关闭方法
                    conn.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String args[]) {
        TRSConnection conn = null;
        TRSResultSet rs = null;
        DBConnector db = new DBConnector();
        try {
            conn = new TRSConnection();
            conn = db.getDBConnection();
            rs = new TRSResultSet();
            rs = conn.executeSelect("Demo3", "作者=李云飞", false);
            System.out.println("共检索到 " + rs.getRecordCount() + "条记录");
            for (int i = 0; i < 100 && i < rs.getRecordCount(); i++) {
                rs.moveTo(0, i);
                System.out.println("第" + i + "条记录");
                System.out.println(rs.getString("日期"));
                System.out.println(rs.getString("版次"));
                System.out.println(rs.getString("作者"));
                System.out.println(rs.getString("标题", "red"));
            }
        } catch (TRSException e) {
            System.out.println("ErrorCode: " + e.getErrorCode());
            System.out.println("ErrorString: " + e.getErrorString());
        } finally {
            if (rs != null) rs.close();
            rs = null;

            if (conn != null) conn.close();
            conn = null;
        }
    }
}
