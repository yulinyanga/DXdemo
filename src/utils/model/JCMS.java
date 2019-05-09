package utils.model;

public class JCMS {
	private String Y_ID;
	private String SIP;
	private String SPORT;
	private String SID;
	private String TB_NAME;
	private String DB_TYPE;
	private String keyword;
	private String trscontent;
	private String db_name;
	public String getDb_name() {
		return db_name;
	}
	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}
	public String getTrscontent() {
		return trscontent;
	}
	public void setTrscontent(String trscontent) {
		this.trscontent = trscontent;
	}
	public String getY_ID() {
		return Y_ID;
	}
	public void setY_ID(String y_ID) {
		Y_ID = y_ID;
	}
	public String getSIP() {
		return SIP;
	}
	public void setSIP(String sIP) {
		SIP = sIP;
	}
	public String getSPORT() {
		return SPORT;
	}
	public void setSPORT(String sPORT) {
		SPORT = sPORT;
	}
	public String getSID() {
		return SID;
	}
	public void setSID(String sID) {
		SID = sID;
	}
	public String getTB_NAME() {
		return TB_NAME;
	}
	public void setTB_NAME(String tB_NAME) {
		TB_NAME = tB_NAME;
	}
	public String getDB_TYPE() {
		return DB_TYPE;
	}
	public void setDB_TYPE(String dB_TYPE) {
		DB_TYPE = dB_TYPE;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	@Override
	public String toString() {
		return "JCMS [Y_ID=" + Y_ID + ", SIP=" + SIP + ", SPORT=" + SPORT + ", SID=" + SID + ", TB_NAME=" + TB_NAME
				+ ", DB_TYPE=" + DB_TYPE + ", keyword=" + keyword + "]";
	}
	
}
