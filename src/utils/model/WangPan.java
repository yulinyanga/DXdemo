package utils.model;

public class WangPan {
	private String ip;
	private String type;
	private String filepath;
	private String keyword;
	public String getKeyword() {
		return keyword;
	}
	@Override
	public String toString() {
		return "WangPan [ip=" + ip + ", type=" + type + ", filepath=" + filepath + ", keyword=" + keyword + "]";
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
}
