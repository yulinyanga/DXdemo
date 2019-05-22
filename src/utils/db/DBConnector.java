package utils.db;

import com.eprobiti.trs.TRSConnection;
import com.eprobiti.trs.TRSException;
import utils.count.CountSensitiveWords;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

/**
 * ���ݿ�����
 *
 * @author Hermes
 */
public class DBConnector {
    //todo �ڵ���ǰ���� ip ���� ���ݿ����� ���ʽ ���·��(desktop��countFile��detailFile��logPath)
//    private final static String DB_URL = "localhost";      //TRSServer���ݿ�URL
    //private final static String DB_URL = "10.72.77.90";	  //TRSServer���ݿ�URL
    //private final static String DB_URL = "10.72.76.187";	  //TRSServer���ݿ�URL
//    private final static String DB_URL = "10.78.57.32";      //TRSServer���ݿ�URL
        	private final static String DB_URL = "10.72.76.89";	  //TRSServer���ݿ�URL
    private final static String DB_PORT = "8888";               //TRSServer�˿�
    //    private final static String DB_URL = "10.72.76.73";      //TRSServer���ݿ�URL
    private final static String DB_USERNAME = "system";            //TRSServer�û���
    private final static String DB_PASSWORD = "manager";         //TRSServer����
    public final static String serverTable = "WeiBo0521";        //TRSServer���ݿ���  WeiXin  WeiBo
    public final static String groupType = "΢��";            //���ݿ�����  ��Ϊ  �ⲿ��վ���ڲ���վ��΢����΢��
    public final static String desktop = "C:\\Users\\11633\\Desktop\\";
    //    public final static String biaodashi = desktop + "��Ϣ����\\0507�������ʽ.txt";        //�������ʽ
    public final static String biaodashi = desktop + "��Ϣ����\\0514�������ʽ.txt";        //�������ʽ
    public final static String countFile = desktop + "��Ϣ����\\������Ϣ\\" + groupType + "_������Ϣͳ��_" + LocalDate.now() + "_" + serverTable + ".xlsx";        //���������Ϣͳ��
    public final static String detailFile = desktop + "��Ϣ����\\������Ϣ\\" + groupType + "_������Ϣ��ϸ_" + (new Date().getMonth() + 1) + "��" + (new Date().getDate()) + "��" + (new Date()).getHours() + "ʱ_" + serverTable + ".xlsx";        //���������Ϣ��ϸ
    public final static String logPath = desktop + "��Ϣ����\\log.txt";        //���������Ϣ��ϸ
    public final static String siteList = "";                                     //��������sitename

    /**
     * ����
     *
     * @param args
     */
    public static void main(String args[]) throws IOException {
        //ͳ���б�
        CountSensitiveWords.countBySize();
        //��ϸ��Ϣ
        CountSensitiveWords.detail();
    }

    /**
     * ��ȡ���ݿ�����
     *
     * @return
     */
    public TRSConnection getDBConnection() {
        TRSConnection conn = null;
        try {
            conn = new TRSConnection();
            conn.connect(DB_URL, DB_PORT, DB_USERNAME, DB_PASSWORD, serverTable);
        } catch (TRSException e) {
            System.out.println("ErrorCode: " + e.getErrorCode());
            System.out.println("ErrorString: " + e.getErrorString());
        }
        return conn;
    }

    /**
     * �ر����ݿ�����
     *
     * @param
     */
    public void closeConnection(TRSConnection conn) {
        try {
            if (conn != null) {
                if (!conn.isClosed()) {   //�жϵ�ǰ�������Ӷ������û�б��رվ͵��ùرշ���
                    conn.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
