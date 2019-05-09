package util.test;

import java.util.List;

import com.eprobiti.trs.TRSColProperty;
import com.eprobiti.trs.TRSConnection;
import com.eprobiti.trs.TRSDataBase;
import com.eprobiti.trs.TRSException;
import com.eprobiti.trs.TRSResultSet;

import utils.db.DBConnector;

public class TestAllData {
	static DBConnector db = new DBConnector();
	static TRSConnection conn = null;
	static TRSResultSet rs = null;
	public static String getColumnName(String tableName) {
		long num=0;
		conn=db.getDBConnection();
		TRSColProperty col = null;
		try {
			col = conn.getStatCol(tableName,0);
			System.out.println(col.strColumn + ":" + col.IndexMinus);
		} catch (TRSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return col.strColumn;
	}
	
	public static void main(String[] args) {
		getColumnName("AS$B$20$1");
	}
}
