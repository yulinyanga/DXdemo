/*
package util.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.eprobiti.trs.TRSConnection;
import com.eprobiti.trs.TRSException;
import com.eprobiti.trs.TRSResultSet;

import utils.count.ConcatSiteName;
import utils.count.CountSensitiveWords;
import utils.count.CountTotalRecordNum;
import utils.count.CreateTable;
import utils.db.DBConnector;
import utils.model.AuthorModel;
import utils.model.FIleModel;
import utils.model.JCMS;
import utils.model.SpecialWord;
import utils.model.WangPan;
import utils.model.WeiXin;

public class BeiFen {
	private static final boolean String = false;
	private String reallyip="";
	private String tablename="";
	private String checkpage="";
	String[] where = {"(IR_SITENAME=集团公司官网) and (IR_URLCONTENT=","(IR_SITENAME=OA系统首页的三个新闻栏目) and (IR_URLCONTENT=","(IR_SITENAME=%工程信息综合管理平台%) and (IR_URLCONTENT=","(IR_SITENAME=oA系统新闻中心页面) and (IR_URLCONTENT="};
	String[] filePath = new String[] {"D://chinese.txt","D://english.txt","D://traditional20190419.txt"};
	DBConnector db = new DBConnector();
	TRSConnection conn = null;
	TRSResultSet rs = null;

	*/
/**
	 * 统计包含敏感词记录的条数
	 * @param tableName
	 * @param sensitiveWords
	 * @param isContinue
	 * @return 条数
	 *//*

	public long count(String tableName, String sensitiveWords, boolean isContinue) {
		long num=0;
		conn=db.getDBConnection();
		try{
			rs = new TRSResultSet();
			rs = conn.executeSelect(tableName, sensitiveWords, isContinue);
			num = rs.getRecordCount();
			//for (int i = 0; i < 3 && i < rs.getRecordCount(); i++){
			for (int i = 0; i < rs.getRecordCount(); i++){
				rs.moveTo(0, i);
				System.out.println();
				System.out.println("第" + i + "条记录");
				//System.out.println(rs.getString("Y_ID"));
				//String words = rs.getString("TRSCONTENT","red");
				//System.out.println(rs.getString("id"));
				//String words = rs.getString("content","red");
				System.out.println(rs.getString("IR_SID"));
				String words = rs.getString("IR_URLCONTENT","red");
				String[] wordsArray = words.split("<font color=red>");
				String searchWords = "";
				for(int j=1; j<wordsArray.length; j++){
					int index = wordsArray[j].indexOf("</font>");
					wordsArray[j] = wordsArray[j].substring(0,index);
					searchWords += wordsArray[j]+",";
				}
				System.out.println(searchWords);
				System.out.println("敏感词出现的次数：" + sameWordNums(searchWords));
			}
		}
		catch (TRSException e){
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("ErrorString: " + e.getErrorString());
		}
		finally{
			db.closeConnection(conn);
		}
		return num;
	}
	*/
/**
	 * 查找敏感词的相关信息的详细信息
	 * @param tableName
	 * @param sensitiveWords
	 * @param isContinue
	 * @param serverTable 
	 * @return 详细信息
	 * @throws IOException 
	 *//*

	public List searchMessage(String tableName, String sensitiveWords, boolean isContinue) throws IOException {
		long num=0;
		conn=db.getDBConnection();
		//所有的疑似信息列表
		List list=null;
		try{
			rs = new TRSResultSet();
			rs = conn.executeSelect(tableName, sensitiveWords, isContinue);
			System.out.println(sensitiveWords);
			num = rs.getRecordCount();
			//循环添加疑似信息
			String log="";
			String column=ConcatSiteName.getColumnName(tableName);
			String column3=ConcatSiteName.getColumnName3(tableName);
			if("IR_HKEY".equals(column)) {
				list=new LinkedList<WeiXin>();
				for (int i = 0; i < rs.getRecordCount(); i++){
					rs.moveTo(0, i);
					WeiXin weiXin=new WeiXin();
					weiXin.setFileName(rs.getString("IR_AUTHORS"));
					weiXin.setIR_URLNAME(rs.getString("IR_URLNAME"));
					weiXin.setIR_WEIXINID(rs.getString("IR_WEIXINID"));
					weiXin.setIR_CONTENT(rs.getString("IR_CONTENT"));
					String words = rs.getString("IR_CONTENT","red");
					String[] wordsArray = words.split("<font color=red>");
					String searchWords = "";
					for(int j=1; j<wordsArray.length; j++){
						int index = wordsArray[j].indexOf("</font>");
						wordsArray[j] = wordsArray[j].substring(0,index);
						searchWords += wordsArray[j]+",";
					}
					weiXin.setKeyword(sameWordNums(searchWords));
					list.add(weiXin);
				}
			} if("IR_SID".equals(column)) {
				list=new LinkedList();
				for (int i = 0; i < rs.getRecordCount(); i++){
					SpecialWord specialWord=new SpecialWord();

					rs.moveTo(0, i);
					String id= rs.getString("IR_SID");
					specialWord.setId(id);
					String words = rs.getString("IR_URLCONTENT","red");
					specialWord.setContent(words);
					String url= rs.getString("IR_URLNAME");
					specialWord.setUrl(url);
					String siteName= rs.getString("IR_SITENAME");
					specialWord.setSiteName(siteName);
					specialWord.setChannel(rs.getString("IR_CHANNEL"));
					String[] wordsArray = words.split("<font color=red>");
					String searchWords = "";
					String groupname=rs.getString("IR_GROUPNAME");
					specialWord.setGroupname(groupname);
					for(int j=1; j<wordsArray.length; j++){
						int index = wordsArray[j].indexOf("</font>");
						wordsArray[j] = wordsArray[j].substring(0,index);
						searchWords += wordsArray[j]+",";
					}
					specialWord.setKeyword(sameWordNums(searchWords));
					System.out.println("敏感词出现的次数：" + sameWordNums(searchWords));
					list.add(specialWord);
					log+=specialWord.toString()+"\r\n"+"\n"+"\n";
				}
			}else if("IR_AUTHORS".equals(column)) {		//增加微信类型
				list=new LinkedList();
				for (int i = 0; i < rs.getRecordCount(); i++){
					SpecialWord specialWord=new SpecialWord();
					rs.moveTo(0, i);
					String id= rs.getString("IR_SID");
					specialWord.setId(id);
					String words = rs.getString("IR_URLCONTENT","red");
					specialWord.setContent(words);
					String url= rs.getString("IR_URLNAME");
					specialWord.setUrl(url);
					String siteName= rs.getString("IR_SITENAME");
					specialWord.setSiteName(siteName);
					String[] wordsArray = words.split("<font color=red>");
					String searchWords = "";
					String groupname=rs.getString("IR_GROUPNAME");
					specialWord.setGroupname(groupname);
					for(int j=1; j<wordsArray.length; j++){
						int index = wordsArray[j].indexOf("</font>");
						wordsArray[j] = wordsArray[j].substring(0,index);
						searchWords += wordsArray[j]+",";
					}
					specialWord.setKeyword(sameWordNums(searchWords));
					list.add(specialWord);
					log+=specialWord.toString()+"\r\n"+"\n"+"\n";
				}
			}else if("IR_CREATED_AT".equals(column)) {		//增加微博类型
				list=new LinkedList();
				for (int i = 0; i < rs.getRecordCount(); i++){
					SpecialWord specialWord=new SpecialWord();
					rs.moveTo(0, i);
					String id= rs.getString("IR_SID");
					specialWord.setId(id);
					String words = rs.getString("IR_URLCONTENT","red");
					specialWord.setContent(words);
					String url= rs.getString("IR_URLNAME");
					specialWord.setUrl(url);
					String siteName= rs.getString("IR_SITENAME");
					specialWord.setSiteName(siteName);
					String[] wordsArray = words.split("<font color=red>");
					String searchWords = "";
					String groupname=rs.getString("IR_GROUPNAME");
					specialWord.setGroupname(groupname);
					for(int j=1; j<wordsArray.length; j++){
						int index = wordsArray[j].indexOf("</font>");
						wordsArray[j] = wordsArray[j].substring(0,index);
						searchWords += wordsArray[j]+",";
					}
					specialWord.setKeyword(sameWordNums(searchWords));
					list.add(specialWord);
					log+=specialWord.toString()+"\r\n"+"\n"+"\n";
				}
			}else if ("FILENAME".equals(column)) {
				list=new LinkedList();
				for (int i = 0; i < rs.getRecordCount(); i++){
					WangPan wangPan=new WangPan();
					wangPan.setIp(rs.getString("IP"));
					wangPan.setFilepath(rs.getString("FILEPATH"));
					wangPan.setType(rs.getString("TYPE"));
					log+=wangPan.toString()+"\r\n"+"\n"+"\n";
					list.add(wangPan);
				}
			}else if ("Y_ID".equals(column)) {
				list=new LinkedList<JCMS>();
				for (int i = 0; i < rs.getRecordCount(); i++){
					JCMS jcms=new JCMS();
					jcms.setDB_TYPE(rs.getString("DB_TYPE"));
					jcms.setSID(rs.getString("SID"));
					jcms.setSIP(rs.getString("SIP"));
					jcms.setSPORT(rs.getString("SPORT"));
					jcms.setTB_NAME(rs.getString("TB_NAME"));
					jcms.setY_ID(rs.getString("MID"));
					jcms.setTrscontent(rs.getString("TRSCONTENT"));
					jcms.setDb_name(rs.getString("DB_NAME"));
					String words = rs.getString("TRSCONTENT","red");
					String[] wordsArray = words.split("<font color=red>");
					String searchWords = "";
					for(int j=1; j<wordsArray.length; j++){
						int index = wordsArray[j].indexOf("</font>");
						wordsArray[j] = wordsArray[j].substring(0,index);
						searchWords += wordsArray[j]+",";
					}
					jcms.setKeyword(sameWordNums(searchWords));
					list.add(jcms);
				}
			}else if ("FileName".equals(column)) {
				list=new LinkedList<FIleModel>();
				for (int i = 0; i < rs.getRecordCount(); i++){
					rs.moveTo(0, i);
					FIleModel fIleModel=new FIleModel();
					fIleModel.setFilename(rs.getString("FileName"));
					fIleModel.setFilepath(rs.getString("FilePath"));
					fIleModel.setIp(rs.getString("ip"));
					String words = rs.getString("WP_Content","red");
					String[] wordsArray = words.split("<font color=red>");
					String searchWords = "";
					for(int j=1; j<wordsArray.length; j++){
						int index = wordsArray[j].indexOf("</font>");
						wordsArray[j] = wordsArray[j].substring(0,index);
						searchWords += wordsArray[j]+",";
					}
					fIleModel.setKey(sameWordNums(searchWords));
					System.out.println("敏感词出现的次数：" + sameWordNums(searchWords));
					list.add(fIleModel);
				}
			}else if ("id".equals(column)) {
				list=new LinkedList<FIleModel>();
				for (int i = 0; i < rs.getRecordCount(); i++){
					rs.moveTo(0, i);
					System.out.println("i"+i+"rs.getRecordCount()"+rs.getRecordCount());
					AuthorModel authorModel=new AuthorModel();
					authorModel.setAuthor_id(rs.getString("author_id"));
					authorModel.setAuthor_name(rs.getString("author"));
					authorModel.setMid(rs.getString("MID"));
					authorModel.setSip(rs.getString("SIP"));
					String words = rs.getString("trscontent","red");
					String[] wordsArray = words.split("<font color=red>");
					String searchWords = "";
					for(int j=1; j<wordsArray.length; j++){
						int index = wordsArray[j].indexOf("</font>");
						wordsArray[j] = wordsArray[j].substring(0,index);
						searchWords += wordsArray[j]+",";
					}
					authorModel.setKeyword(sameWordNums(searchWords));
					list.add(authorModel);
				}
			}

			File file=new File("D://log.txt");
			FileOutputStream fileOutputStream=new FileOutputStream(file);
			fileOutputStream.write(log.getBytes());
			fileOutputStream.close();
		}
		catch (TRSException e){
			System.out.println("ErrorCode: " + e.getErrorCode());
			System.out.println("ErrorString: " + e.getErrorString());
		}
		finally{
			db.closeConnection(conn);
		}
		return list;
	}

	*/
/**
	 * 获取相应目录下文本中所有的敏感词
	 * @param filePath
	 * @return 文本中所有的敏感词
	 *//*

	public static String getSensitiveWords(String filePath){
		String sensitiveWords = "";
		try (FileReader reader = new FileReader(filePath);
				BufferedReader br = new BufferedReader(reader)) {
			sensitiveWords = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sensitiveWords;
	}

	*/
/**
	 * 每条记录出现相同敏感词的次数
	 * @param searchWords
	 * @return 相同敏感词记录
	 *//*

	public static String sameWordNums(String searchWords) {
		String[] string = searchWords.split(",");
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < string.length; i++) {
			if (map.get(string[i]) == null) {
				map.put(string[i], 1);
			} else {
				map.put(string[i], map.get(string[i]) + 1);
			}
		}
		//System.out.println(map.toString());
		return map.toString();
	}

	public static String sameChar(String searchWords) {
		String string = searchWords;
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		for (int i = 0; i < string.length(); i++) {
			Character character = new Character(string.charAt(i));
			if (map.get(character) == null) {
				map.put(character, 1);
			} else {
				map.put(character, map.get(character) + 1);
			}
		}
		//System.out.println(map.toString());
		return map.toString();
	}
	*/
/**
	 * 获取指定数据库的敏感词信息
	 *//*

	public void searchAllMessage() {
		CountSensitiveWords csw = new CountSensitiveWords();
		long recordNum=0;
		long recordTotal=0;
		String serverTable = "AS$6$10$1";
		String[] where = {"(IR_SITENAME=集团公司官网) and (IR_URLCONTENT=","(IR_SITENAME=OA系统首页的三个新闻栏目) and (IR_URLCONTENT=","(IR_SITENAME=%工程信息综合管理平台%) and (IR_URLCONTENT=","(IR_SITENAME=oA系统新闻中心页面) and (IR_URLCONTENT="};
		String[] filePath = new String[] {"D://chinese.txt","D://english.txt","D://traditional20190419.txt"};
		List<SpecialWord> list=new LinkedList<>();

		for(int j=0; j<where.length; j++) {
			for(int i=0; i<filePath.length; i++) {
				try {
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+")",false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("==============================================================================");
				//				System.out.println("以上为  " + serverTable + " 表的站点为" + where[j] + "在此文件的‘" + filePath[i] + "’敏感词范围内，共检索到 " + recordNum + "条记录");
				for(int z=0;z<list.size();z++) {
					SpecialWord specialWord=list.get(z);
					System.out.println("list的详细"+list.get(z).toString());
				}

				System.out.println("");
			}
		}
	}
	public static void main1(String args[]) {
		CountSensitiveWords csw = new CountSensitiveWords();
		long recordNum=0;
		long recordTotal=0;
		//String serverTable = "photo";
		String serverTable = "AS$6$10$1";
		//String filePath = "C://TRS//chinese.txt";
		//String filePath = "C://TRS//english.txt";
		//String filePath = "C://TRS//traditional20190419.txt";
		//String filePath = "C://TRS//testSensitive.txt";
		//recordNum = csw.count(serverTable,getSensitiveWords(filePath),false);
		String[] where = {"(IR_SITENAME=集团公司官网) and (IR_URLCONTENT=","(IR_SITENAME=OA系统首页的三个新闻栏目) and (IR_URLCONTENT=","(IR_SITENAME=%工程信息综合管理平台%) and (IR_URLCONTENT=","(IR_SITENAME=oA系统新闻中心页面) and (IR_URLCONTENT="};
		String[] filePath = new String[] {"D://chinese.txt","D://english.txt","D://traditional20190419.txt"};
		for(int j=0; j<where.length; j++) {
			for(int i=0; i<filePath.length; i++) {
				recordNum = csw.count(serverTable,where[j]+getSensitiveWords(filePath[i])+")",false);
				System.out.println("==============================================================================");
				System.out.println("以上为  " + serverTable + " 表的站点为" + where[j] + "在此文件的‘" + filePath[i] + "’敏感词范围内，共检索到 " + recordNum + "条记录");
				System.out.println("");
			}
		}

		for(int i=0; i<filePath.length; i++) {
			//recordTotal = csw.count(serverTable, "", false);
			recordNum = csw.count(serverTable,getSensitiveWords(filePath[i]),false);
			System.out.println("==============================================================================");
			System.out.println("以上为  " + serverTable + " 表，记录一共" + recordTotal + "在此文件的‘" + filePath[i] + "’敏感词范围内，共检索到 " + recordNum + "条记录");
			System.out.println("");
		}


	}
	public static void main(String[] args) throws IOException {
		CountSensitiveWords csw = new CountSensitiveWords();
		long recordNum=0;
		long recordTotal=0;
		String serverTable = DBConnector.serverTable;
		//String[] where = {"(IR_SITENAME=集团公司官网) and (IR_URLCONTENT=","(IR_SITENAME=OA系统首页的三个新闻栏目) and (IR_URLCONTENT=","(IR_SITENAME=%工程信息综合管理平台%) and (IR_URLCONTENT=","(IR_SITENAME=oA系统新闻中心页面) and (IR_URLCONTENT="};
		//String[] where= {"(IR_SITENAME=海臻汇) and (IR_URLCONTENT=","(IR_SITENAME=工业水处理) and (IR_URLCONTENT=","(IR_SITENAME=%中海油天津化工研究设计院有限公司网站%) and (IR_URLCONTENT=","(IR_SITENAME=无机盐工业) and (IR_URLCONTENT=","(IR_SITENAME=%涂料工业网站%) and (IR_URLCONTENT=","(IR_SITENAME=办公OA) and (IR_URLCONTENT=","(IR_SITENAME=部门OA主页) and (IR_URLCONTENT="};
		//String[] where= {"(IR_SITENAME=%涂料工业网站%) and (IR_URLCONTENT="};
		//String[] filePath = new String[] {"D://chinese.txt","D://english.txt","D://traditional20190419.txt"};
		//String[] filePath = new String[] {"D://allword.txt"};
		String  []filePath= {"D://表达式0429外网.txt"};
		String[] where=ConcatSiteName.getAllSiteNAme(serverTable);
		//使用数据库的的列来区分数据库
		String column=ConcatSiteName.getColumnName(serverTable);
		String column3=ConcatSiteName.getColumnName3(serverTable);
		//增加数据库的判断
		if("IR_HKEY".equals(column)) {	
			List<WeiXin> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("微信名称");  
			header.createCell(1).setCellValue("检索数量");  
			header.createCell(2).setCellValue("敏感词数量");  
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);
					if(list.size()>0) {
						q=q+1;
						WeiXin weiXin=list.get(0);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(weiXin.getIR_WEIXINID()); 
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, weiXin.getIR_WEIXINID()));  
						header2.createCell(2).setCellValue(list.size());
						System.out.println("");
					}else {
						list = csw.searchMessage(serverTable,where[j]+""+"%)",false);
						if(list.size()>0) {
							q=q+1;
							WeiXin weiXin=list.get(0);
							XSSFRow header2=sheet.createRow(q);
							header2.createCell(0).setCellValue(weiXin.getIR_WEIXINID()); 
							header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, weiXin.getIR_WEIXINID()));  
							header2.createCell(2).setCellValue(0);
							System.out.println("");
						}
					} 

					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息总数.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}else if("IR_SID".equals(column)) {			//增加微博库的类型
			List<SpecialWord> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("网站名称");
			header.createCell(1).setCellValue("检索总量");  
			header.createCell(2).setCellValue("疑敏感词总量");
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			*/
/*String []str=ConcatSiteName.getAllSiteNAme(serverTable);
			for(int i=0;i<ConcatSiteName.getAllSiteNAme(serverTable).length;i++) {
				XSSFRow header2=sheet.createRow(i+1);
				header2.createCell(0).setCellValue(str[i]);
				header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, str[i]));  
				header2.createCell(2).setCellValue(0);
			}*//*

			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					System.out.println(where[j]+getSensitiveWords(filePath[i])+"%)");
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);
					if(list.size()>0) {
						q=q+1;
						SpecialWord specialWord=list.get(0);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(specialWord.getSiteName());
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, specialWord.getSiteName()));  
						header2.createCell(2).setCellValue(list.size());
						System.out.println("");
					}else {
						list = csw.searchMessage(serverTable,where[j]+""+"%)",false);
						if(list.size()>0) {
							q=q+1;
							SpecialWord specialWord=list.get(0);
							XSSFRow header2=sheet.createRow(q);
							header2.createCell(0).setCellValue(specialWord.getSiteName());
							header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, specialWord.getSiteName()));  
							header2.createCell(2).setCellValue(0);
						}else {
							System.out.println("hahaahahhhhhhhhhhhhhhhhhhh"+where[j]+""+"%)");
						}
					}


				}
			}
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息分网站统计.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}else if("IR_AUTHORS".equals(column)) {			//增加微信库的类型
			List<SpecialWord> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("IR_ID");
			header.createCell(1).setCellValue("链接地址");
			header.createCell(2).setCellValue("链接内容");  
			header.createCell(3).setCellValue("网站名称");
			header.createCell(4).setCellValue("组名");  
			header.createCell(5).setCellValue("检索词");
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);
					for(int z=1;z<list.size()+1;z++) {
						q=q+1;
						SpecialWord specialWord=list.get(z-1);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(specialWord.getId());
						header2.createCell(1).setCellValue(specialWord.getUrl());
						header2.createCell(2).setCellValue(specialWord.getContent());  
						header2.createCell(3).setCellValue(specialWord.getSiteName());
						header2.createCell(4).setCellValue(specialWord.getGroupname());  
						header2.createCell(5).setCellValue(specialWord.getKeyword());
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息分网站统计.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}else if("wangpan".equals(serverTable)) {
			List<WangPan> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("ip");
			header.createCell(1).setCellValue("类型");
			header.createCell(2).setCellValue("文件地址");  
			header.createCell(3).setCellValue("检索词");
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+")",false);
					System.out.println("==============================================================================");
					//				System.out.println("以上为  " + serverTable + " 表的站点为" + where[j] + "在此文件的‘" + filePath[i] + "’敏感词范围内，共检索到 " + recordNum + "条记录");
					for(int z=0;z<list.size();z++) {
						WangPan wangPan=list.get(z);
						System.out.println("list的详细"+list.get(z).toString());
						XSSFRow header2=sheet.createRow(z+1);
						header2.createCell(0).setCellValue(wangPan.getIp());
						header2.createCell(1).setCellValue(wangPan.getType());
						header2.createCell(2).setCellValue(wangPan.getFilepath());  
						header2.createCell(3).setCellValue(wangPan.getKeyword());
					}

					System.out.println("");
				}
			}
			//设置列的宽度
			//getPhysicalNumberOfCells()代表这行有多少包含数据的列
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				//POI设置列宽度时比较特殊，它的基本单位是1/255个字符大小，
				//因此我们要想让列能够盛的下20个字符的话，就需要用255*20
				sheet.setColumnWidth(i, 255*20);
			}
			//设置行高，行高的单位就是像素，因此30就是30像素的意思
			header.setHeightInPoints(30);
			//上面设置好了内容，我们当然是要输出到某个文件的，输出就需要有输出流
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息总数.xlsx");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}else if("FileName".equals(column)) {
			List<FIleModel> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("文件名");
			header.createCell(1).setCellValue("文件地址");
			header.createCell(2).setCellValue("ip");  
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);

					if(list.size()>0) {
						q=q+1;
						FIleModel fIleModel=list.get(0);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(fIleModel.getIp());
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, fIleModel.getIp()));  
						header2.createCell(2).setCellValue(list.size());
						System.out.println("");
					}else {
						list = csw.searchMessage(serverTable,where[j]+""+"%)",false);
						q=q+1;
						FIleModel fIleModel=list.get(0);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(fIleModel.getIp());
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, fIleModel.getIp()));  
						header2.createCell(2).setCellValue(0);
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息总数.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  

		}else if("id".equals(column)) {
			List<AuthorModel> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息总数列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("作者id");
			header.createCell(1).setCellValue("作者名称");
			header.createCell(2).setCellValue("MID");  
			header.createCell(2).setCellValue("检索词");	  
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);

					if(list.size()>0) {
						AuthorModel authorModel=list.get(0);
						q=q+1;
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(authorModel.getAuthor_id());
						header2.createCell(1).setCellValue(authorModel.getAuthor_name());
						header2.createCell(2).setCellValue(authorModel.getMid());  
						header2.createCell(3).setCellValue(authorModel.getKeyword());  
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, authorModel.getSip()));  
						header2.createCell(2).setCellValue(list.size());
						System.out.println("");
					}else {
						list = csw.searchMessage(serverTable,where[j]+""+"%)",false);
						AuthorModel authorModel=list.get(0);
						q=q+1;
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(authorModel.getAuthor_id());
						header2.createCell(1).setCellValue(authorModel.getAuthor_name());
						header2.createCell(2).setCellValue(authorModel.getMid());  
						header2.createCell(3).setCellValue(authorModel.getKeyword());  
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable,authorModel.getSip()));  
						header2.createCell(2).setCellValue(0);
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  

		}else if("Y_ID".equals(column)) {
			List<JCMS> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息总数列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("Y_ID");
			header.createCell(1).setCellValue("ip");
			header.createCell(2).setCellValue("SPORT");  
			header.createCell(3).setCellValue("数据库名称");  
			header.createCell(4).setCellValue("数据库类型");  
			header.createCell(5).setCellValue("检索词");  
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);

					if(list.size()>0) {
						q=q+1;
						JCMS jcms=list.get(0);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(jcms.getSID());
						header2.createCell(1).setCellValue(jcms.getSIP());
						header2.createCell(2).setCellValue(jcms.getSPORT());  
						header2.createCell(3).setCellValue(jcms.getTB_NAME());  
						header2.createCell(4).setCellValue(jcms.getDB_TYPE());
						header2.createCell(5).setCellValue(jcms.getKeyword()); 
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, jcms.getSIP()));  
						header2.createCell(2).setCellValue(list.size());
						System.out.println("");
					}else {
						list = csw.searchMessage(serverTable,where[j]+""+"%)",false);
						q=q+1;
						JCMS jcms=list.get(0);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(jcms.getSIP());
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, jcms.getSIP()));  
						header2.createCell(2).setCellValue(0);
					}
					for(int z=1;z<list.size()+1;z++) {
						q=q+1;
						JCMS jcms=list.get(z-1);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(jcms.getSID());
						header2.createCell(1).setCellValue(jcms.getSIP());
						header2.createCell(2).setCellValue(jcms.getSPORT());  
						header2.createCell(3).setCellValue(jcms.getTB_NAME());  
						header2.createCell(4).setCellValue(jcms.getDB_TYPE());
						header2.createCell(5).setCellValue(jcms.getKeyword());  
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
		}
	}
	public static void main3(String[] args) throws IOException {
		CountSensitiveWords csw = new CountSensitiveWords();
		long recordNum=0;
		long recordTotal=0;
		String serverTable = DBConnector.serverTable;
		//String[] where = {"(IR_SITENAME=集团公司官网) and (IR_URLCONTENT=","(IR_SITENAME=OA系统首页的三个新闻栏目) and (IR_URLCONTENT=","(IR_SITENAME=%工程信息综合管理平台%) and (IR_URLCONTENT=","(IR_SITENAME=oA系统新闻中心页面) and (IR_URLCONTENT="};
		//String[] where= {"(IR_SITENAME=海臻汇) and (IR_URLCONTENT=","(IR_SITENAME=工业水处理) and (IR_URLCONTENT=","(IR_SITENAME=%中海油天津化工研究设计院有限公司网站%) and (IR_URLCONTENT=","(IR_SITENAME=无机盐工业) and (IR_URLCONTENT=","(IR_SITENAME=%涂料工业网站%) and (IR_URLCONTENT=","(IR_SITENAME=办公OA) and (IR_URLCONTENT=","(IR_SITENAME=部门OA主页) and (IR_URLCONTENT="};
		//String[] where= {"(IR_SITENAME=%涂料工业网站%) and (IR_URLCONTENT="};
		//String[] filePath = new String[] {"D://chinese.txt","D://english.txt","D://traditional20190419.txt"};
		//String[] filePath = new String[] {"D://allword.txt"};
		String  []filePath= {"D://表达式0419.txt"};
		String[] where=ConcatSiteName.getAllSiteNAme(serverTable);
		//使用数据库的的列来区分数据库
		String column=ConcatSiteName.getColumnName(serverTable);
		String column3=ConcatSiteName.getColumnName3(serverTable);
		//增加数据库的判断
		if("IR_HKEY".equals(column)) {	
			List<WeiXin> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("微信名称");  
			header.createCell(1).setCellValue("检索数量");  
			header.createCell(2).setCellValue("敏感词数量");  
			//定义出异常的数量
			HashMap<String,String> hashMapAll=ConcatSiteName.getAllSiteNameData(serverTable);
			HashMap<String,String> hashMapSpecialAll=ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
			int g=1;
			for(String sitename:hashMapAll.keySet()) {
				XSSFRow header2=sheet.createRow(g);
				header.createCell(0).setCellValue(sitename);  
				header.createCell(1).setCellValue(hashMapAll.get(sitename));  
				header.createCell(2).setCellValue(hashMapSpecialAll.get(sitename));  
				g++;
			}
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息总数.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}else if("IR_SID".equals(column)) {			//增加微博库的类型
			List<SpecialWord> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("网站名称");
			header.createCell(1).setCellValue("检索总量");  
			header.createCell(2).setCellValue("疑敏感词总量");


			HashMap<String,Integer> hashMapAll=ConcatSiteName.getAllSiteNameData(serverTable);
			HashMap<String,Integer> hashMapSpecialAll=ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
			int g=1;
			for(String sitename:hashMapAll.keySet()) {
				XSSFRow header2=sheet.createRow(g);
				System.out.println("---------------------------------------------------"+g);
				System.out.println(hashMapAll.get(sitename));
				System.out.println(hashMapSpecialAll.get(sitename));
				header2.createCell(0).setCellValue(sitename+"");  
				header2.createCell(1).setCellValue(hashMapAll.get(sitename)+"");  
				if(!"null".equals(hashMapSpecialAll.get(sitename)+"")) {
					header2.createCell(2).setCellValue(hashMapSpecialAll.get(sitename)); 
				}else {
					header2.createCell(2).setCellValue(0); 
				}
				g++;
			}
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息分网站统计.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}else if("IR_AUTHORS".equals(column)) {			//增加微信库的类型
			List<SpecialWord> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("IR_ID");
			header.createCell(1).setCellValue("链接地址");
			header.createCell(2).setCellValue("链接内容");  
			header.createCell(3).setCellValue("网站名称");
			header.createCell(4).setCellValue("组名");  
			header.createCell(5).setCellValue("检索词");
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);
					for(int z=1;z<list.size()+1;z++) {
						q=q+1;
						SpecialWord specialWord=list.get(z-1);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(specialWord.getId());
						header2.createCell(1).setCellValue(specialWord.getUrl());
						header2.createCell(2).setCellValue(specialWord.getContent());  
						header2.createCell(3).setCellValue(specialWord.getSiteName());
						header2.createCell(4).setCellValue(specialWord.getGroupname());  
						header2.createCell(5).setCellValue(specialWord.getKeyword());
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息分网站统计.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}else if("wangpan".equals(serverTable)) {
			List<WangPan> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("ip");
			header.createCell(1).setCellValue("类型");
			header.createCell(2).setCellValue("文件地址");  
			header.createCell(3).setCellValue("检索词");
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+")",false);
					System.out.println("==============================================================================");
					//				System.out.println("以上为  " + serverTable + " 表的站点为" + where[j] + "在此文件的‘" + filePath[i] + "’敏感词范围内，共检索到 " + recordNum + "条记录");
					for(int z=0;z<list.size();z++) {
						WangPan wangPan=list.get(z);
						System.out.println("list的详细"+list.get(z).toString());
						XSSFRow header2=sheet.createRow(z+1);
						header2.createCell(0).setCellValue(wangPan.getIp());
						header2.createCell(1).setCellValue(wangPan.getType());
						header2.createCell(2).setCellValue(wangPan.getFilepath());  
						header2.createCell(3).setCellValue(wangPan.getKeyword());
					}

					System.out.println("");
				}
			}
			//设置列的宽度
			//getPhysicalNumberOfCells()代表这行有多少包含数据的列
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				//POI设置列宽度时比较特殊，它的基本单位是1/255个字符大小，
				//因此我们要想让列能够盛的下20个字符的话，就需要用255*20
				sheet.setColumnWidth(i, 255*20);
			}
			//设置行高，行高的单位就是像素，因此30就是30像素的意思
			header.setHeightInPoints(30);
			//上面设置好了内容，我们当然是要输出到某个文件的，输出就需要有输出流
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息总数.xlsx");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}else if("FileName".equals(column)) {
			List<FIleModel> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("文件名");
			header.createCell(1).setCellValue("文件地址");
			header.createCell(2).setCellValue("ip");  
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);

					if(list.size()>0) {
						q=q+1;
						FIleModel fIleModel=list.get(0);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(fIleModel.getIp());
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, fIleModel.getIp()));  
						header2.createCell(2).setCellValue(list.size());
						System.out.println("");
					}else {
						list = csw.searchMessage(serverTable,where[j]+""+"%)",false);
						q=q+1;
						FIleModel fIleModel=list.get(0);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(fIleModel.getIp());
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, fIleModel.getIp()));  
						header2.createCell(2).setCellValue(0);
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息总数.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  

		}else if("id".equals(column)) {
			List<AuthorModel> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息总数列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("作者id");
			header.createCell(1).setCellValue("作者名称");
			header.createCell(2).setCellValue("MID");  
			header.createCell(2).setCellValue("检索词");	  
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);

					if(list.size()>0) {
						AuthorModel authorModel=list.get(0);
						q=q+1;
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(authorModel.getAuthor_id());
						header2.createCell(1).setCellValue(authorModel.getAuthor_name());
						header2.createCell(2).setCellValue(authorModel.getMid());  
						header2.createCell(3).setCellValue(authorModel.getKeyword());  
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, authorModel.getSip()));  
						header2.createCell(2).setCellValue(list.size());
						System.out.println("");
					}else {
						list = csw.searchMessage(serverTable,where[j]+""+"%)",false);
						AuthorModel authorModel=list.get(0);
						q=q+1;
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(authorModel.getAuthor_id());
						header2.createCell(1).setCellValue(authorModel.getAuthor_name());
						header2.createCell(2).setCellValue(authorModel.getMid());  
						header2.createCell(3).setCellValue(authorModel.getKeyword());  
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable,authorModel.getSip()));  
						header2.createCell(2).setCellValue(0);
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  

		}else if("Y_ID".equals(column)) {
			List<JCMS> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息总数列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("Y_ID");
			header.createCell(1).setCellValue("ip");
			header.createCell(2).setCellValue("SPORT");  
			header.createCell(3).setCellValue("数据库名称");  
			header.createCell(4).setCellValue("数据库类型");  
			header.createCell(5).setCellValue("检索词");  
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);

					if(list.size()>0) {
						q=q+1;
						JCMS jcms=list.get(0);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(jcms.getSID());
						header2.createCell(1).setCellValue(jcms.getSIP());
						header2.createCell(2).setCellValue(jcms.getSPORT());  
						header2.createCell(3).setCellValue(jcms.getTB_NAME());  
						header2.createCell(4).setCellValue(jcms.getDB_TYPE());
						header2.createCell(5).setCellValue(jcms.getKeyword()); 
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, jcms.getSIP()));  
						header2.createCell(2).setCellValue(list.size());
						System.out.println("");
					}else {
						list = csw.searchMessage(serverTable,where[j]+""+"%)",false);
						q=q+1;
						JCMS jcms=list.get(0);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(jcms.getSIP());
						header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, jcms.getSIP()));  
						header2.createCell(2).setCellValue(0);
					}
					for(int z=1;z<list.size()+1;z++) {
						q=q+1;
						JCMS jcms=list.get(z-1);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(jcms.getSID());
						header2.createCell(1).setCellValue(jcms.getSIP());
						header2.createCell(2).setCellValue(jcms.getSPORT());  
						header2.createCell(3).setCellValue(jcms.getTB_NAME());  
						header2.createCell(4).setCellValue(jcms.getDB_TYPE());
						header2.createCell(5).setCellValue(jcms.getKeyword());  
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
		}
	}
	*/
/**
	 * 这个主方法是为了生成疑似信息详细列表
	 *//*

	public static void main0(String[] args) throws IOException {
		CountSensitiveWords csw = new CountSensitiveWords();
		long recordNum=0;
		long recordTotal=0;
		String serverTable = DBConnector.serverTable;
		//String[] where = {"(IR_SITENAME=集团公司官网) and (IR_URLCONTENT=","(IR_SITENAME=OA系统首页的三个新闻栏目) and (IR_URLCONTENT=","(IR_SITENAME=%工程信息综合管理平台%) and (IR_URLCONTENT=","(IR_SITENAME=oA系统新闻中心页面) and (IR_URLCONTENT="};
		//String[] where= {"(IR_SITENAME=海臻汇) and (IR_URLCONTENT=","(IR_SITENAME=工业水处理) and (IR_URLCONTENT=","(IR_SITENAME=%中海油天津化工研究设计院有限公司网站%) and (IR_URLCONTENT=","(IR_SITENAME=无机盐工业) and (IR_URLCONTENT=","(IR_SITENAME=%涂料工业网站%) and (IR_URLCONTENT=","(IR_SITENAME=办公OA) and (IR_URLCONTENT=","(IR_SITENAME=部门OA主页) and (IR_URLCONTENT="};
		//String[] where= {"(IR_SITENAME=%涂料工业网站%) and (IR_URLCONTENT="};
		//String[] filePath = new String[] {"D://chinese.txt","D://english.txt","D://traditional20190419.txt"};
		//String[]  filePath= new String[] {"D://表达式0419.txt"};
		String[]  filePath= new String[] {"D://表达式0419.txt"};
		String[] where=ConcatSiteName.getAllSiteNAme(serverTable);
		//String[] where= {"WP_Content=(%"};
		//使用数据库的的列来区分数据库
		String column=ConcatSiteName.getColumnName(serverTable);
		String column3=ConcatSiteName.getColumnName3(serverTable);
		//增加数据库的判断
		if("IR_HKEY".equals(column)) {	
			List<WeiXin> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("作者名");
			header.createCell(1).setCellValue("链接地址");
			header.createCell(2).setCellValue("微信id");  
			header.createCell(3).setCellValue("文本内容");  
			header.createCell(4).setCellValue("检索词");  
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);
					for(int z=1;z<list.size()+1;z++) {
						q=q+1;
						WeiXin weiXin=list.get(z-1);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(weiXin.getFileName());
						header2.createCell(1).setCellValue(weiXin.getIR_URLNAME()); 
						header2.createCell(2).setCellValue(weiXin.getIR_WEIXINID()); 
						header2.createCell(3).setCellValue(weiXin.getIR_CONTENT());  
						header2.createCell(4).setCellValue(weiXin.getKeyword()); 
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息详细.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}else if("IR_SID".equals(column)) {			//增加微博库的类型
			List<SpecialWord> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("IR_ID");
			header.createCell(1).setCellValue("链接地址");
			header.createCell(2).setCellValue("链接内容");  
			header.createCell(3).setCellValue("网站名称");
			header.createCell(4).setCellValue("频道名称");
			header.createCell(5).setCellValue("组名");  
			header.createCell(6).setCellValue("检索词");
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);
					for(int z=1;z<list.size()+1;z++) {
						q=q+1;
						SpecialWord specialWord=list.get(z-1);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(specialWord.getId());
						header2.createCell(1).setCellValue(specialWord.getUrl());
						header2.createCell(2).setCellValue(specialWord.getContent());  
						header2.createCell(3).setCellValue(specialWord.getSiteName());
						header2.createCell(4).setCellValue(specialWord.getChannel());
						header2.createCell(5).setCellValue(specialWord.getGroupname());  
						header2.createCell(6).setCellValue(specialWord.getKeyword());
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息详细.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}else if("IR_AUTHORS".equals(column)) {			//增加微信库的类型
			List<SpecialWord> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("IR_ID");
			header.createCell(1).setCellValue("链接地址");
			header.createCell(2).setCellValue("链接内容");  
			header.createCell(3).setCellValue("网站名称");
			header.createCell(4).setCellValue("组名");  
			header.createCell(5).setCellValue("检索词");
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);
					for(int z=1;z<list.size()+1;z++) {
						q=q+1;
						SpecialWord specialWord=list.get(z-1);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(specialWord.getId());
						header2.createCell(1).setCellValue(specialWord.getUrl());
						header2.createCell(2).setCellValue(specialWord.getContent());  
						header2.createCell(3).setCellValue(specialWord.getSiteName());
						header2.createCell(4).setCellValue(specialWord.getGroupname());  
						header2.createCell(5).setCellValue(specialWord.getKeyword());
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息详细.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}else if("wangpan".equals(serverTable)) {
			List<WangPan> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("ip");
			header.createCell(1).setCellValue("类型");
			header.createCell(2).setCellValue("文件地址");  
			header.createCell(3).setCellValue("检索词");
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+")",false);
					System.out.println("==============================================================================");
					//				System.out.println("以上为  " + serverTable + " 表的站点为" + where[j] + "在此文件的‘" + filePath[i] + "’敏感词范围内，共检索到 " + recordNum + "条记录");
					for(int z=0;z<list.size();z++) {
						WangPan wangPan=list.get(z);
						System.out.println("list的详细"+list.get(z).toString());
						XSSFRow header2=sheet.createRow(z+1);
						header2.createCell(0).setCellValue(wangPan.getIp());
						header2.createCell(1).setCellValue(wangPan.getType());
						header2.createCell(2).setCellValue(wangPan.getFilepath());  
						header2.createCell(3).setCellValue(wangPan.getKeyword());
					}

					System.out.println("");
				}
			}
			//设置列的宽度
			//getPhysicalNumberOfCells()代表这行有多少包含数据的列
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				//POI设置列宽度时比较特殊，它的基本单位是1/255个字符大小，
				//因此我们要想让列能够盛的下20个字符的话，就需要用255*20
				sheet.setColumnWidth(i, 255*20);
			}
			//设置行高，行高的单位就是像素，因此30就是30像素的意思
			header.setHeightInPoints(30);
			//上面设置好了内容，我们当然是要输出到某个文件的，输出就需要有输出流
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息详细.xlsx");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}else if("FileName".equals(column)) {
			List<FIleModel> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("文件名");
			header.createCell(1).setCellValue("文件地址");
			header.createCell(2).setCellValue("ip");  
			header.createCell(2).setCellValue("检索词");  
			//定义出异常的数量
			int bugNum=0;
			int q=0;

			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);
					for(int z=1;z<list.size()+1;z++) {
						System.out.println("haahahahaha");
						q=q+1;
						FIleModel fIleModel=list.get(z-1);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(fIleModel.getFilename());
						header2.createCell(1).setCellValue(fIleModel.getFilepath());
						header2.createCell(2).setCellValue(fIleModel.getIp());  
						header2.createCell(3).setCellValue(fIleModel.getKey());  
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息详细.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  

		}else if("id".equals(column)) {
			List<AuthorModel> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("作者id");
			header.createCell(1).setCellValue("作者名称");
			header.createCell(2).setCellValue("MID");  
			header.createCell(2).setCellValue("检索词");	  
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%')",false);
					for(int z=1;z<list.size()+1;z++) {
						q=q+1;
						AuthorModel authorModel=list.get(z-1);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(authorModel.getAuthor_id());
						header2.createCell(1).setCellValue(authorModel.getAuthor_name());
						header2.createCell(2).setCellValue(authorModel.getMid());  
						header2.createCell(3).setCellValue(authorModel.getKeyword());  
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息详细.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  

		}else if("Y_ID".equals(column)) {
			List<JCMS> list=new LinkedList<>();
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("疑似信息列表");
			XSSFRow header=sheet.createRow(0);
			header.createCell(0).setCellValue("w_ID");
			header.createCell(1).setCellValue("ip");
			header.createCell(2).setCellValue("数据库名称");  
			header.createCell(3).setCellValue("表名称");  
			header.createCell(4).setCellValue("文本");  
			header.createCell(5).setCellValue("检索词");  
			//定义出异常的数量
			int bugNum=0;
			int q=0;
			for(int j=0; j<where.length; j++) {
				for(int i=0; i<filePath.length; i++) {
					String str=where[j]+getSensitiveWords(filePath[i])+"%)";
					list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);
					for(int z=1;z<list.size()+1;z++) {
						q=q+1;
						JCMS jcms=list.get(z-1);
						XSSFRow header2=sheet.createRow(q);
						header2.createCell(0).setCellValue(jcms.getSID());
						header2.createCell(1).setCellValue(jcms.getSIP());
						header2.createCell(2).setCellValue(jcms.getDb_name());  
						header2.createCell(3).setCellValue(jcms.getTB_NAME());  
						header2.createCell(4).setCellValue(jcms.getTrscontent());
						header2.createCell(5).setCellValue(jcms.getKeyword());  
					}
					bugNum=bugNum+list.size();
					System.out.println("");
				}
			}
			header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
			//设置列的宽度
			for(int i=0;i<header.getPhysicalNumberOfCells();i++){
				sheet.setColumnWidth(i, 255*20);
			}
			header.setHeightInPoints(30);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream("d:/疑似信息详细.xlsx");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//向指定文件写入内容
			try {
				wb.write(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  

		}
	}
		*/
/**
		 * 分网站统计
		 * @param args
		 * @throws IOException
		 *//*

		public static void main4(String[] args) throws IOException {
			CountSensitiveWords csw = new CountSensitiveWords();
			long recordNum=0;
			long recordTotal=0;
			String serverTable = DBConnector.serverTable;
			//String[] where = {"(IR_SITENAME=集团公司官网) and (IR_URLCONTENT=","(IR_SITENAME=OA系统首页的三个新闻栏目) and (IR_URLCONTENT=","(IR_SITENAME=%工程信息综合管理平台%) and (IR_URLCONTENT=","(IR_SITENAME=oA系统新闻中心页面) and (IR_URLCONTENT="};
			//String[] where= {"(IR_SITENAME=海臻汇) and (IR_URLCONTENT=","(IR_SITENAME=工业水处理) and (IR_URLCONTENT=","(IR_SITENAME=%中海油天津化工研究设计院有限公司网站%) and (IR_URLCONTENT=","(IR_SITENAME=无机盐工业) and (IR_URLCONTENT=","(IR_SITENAME=%涂料工业网站%) and (IR_URLCONTENT=","(IR_SITENAME=办公OA) and (IR_URLCONTENT=","(IR_SITENAME=部门OA主页) and (IR_URLCONTENT="};
			//String[] where= {"(IR_SITENAME=%涂料工业网站%) and (IR_URLCONTENT="};
			//String[] filePath = new String[] {"D://chinese.txt","D://english.txt","D://traditional20190419.txt"};
			//String[] filePath = new String[] {"D://allword.txt"};
			String  []filePath= {"D://表达式0419.txt"};
			String[] where=ConcatSiteName.getAllSiteNAme(serverTable);
			//使用数据库的的列来区分数据库
			String column=ConcatSiteName.getColumnName(serverTable);
			String column3=ConcatSiteName.getColumnName3(serverTable);
			//增加数据库的判断
			if("IR_HKEY".equals(column)) {	
				//增加敏感词的map集合
				HashMap<String,Integer> hashMapSpecialAll=ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
				String columnname="微信名称";
				//创建表格
				CreateTable.create(serverTable, columnname, hashMapSpecialAll);
			}else if("IR_SID".equals(column)) {			//增加微博库的类型
				HashMap<String,Integer> hashMapSpecialAll=ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
				String columnname="网站名称";
				//创建表格
				CreateTable.create(serverTable, columnname, hashMapSpecialAll);
			}else if("FileName".equals(column)) {
				HashMap<String,Integer> hashMapSpecialAll=ConcatSiteName.getSpecialSiteName(serverTable, getSensitiveWords(filePath[0]));
				String columnname="ip名称";
				//创建表格
				CreateTable.create(serverTable, columnname, hashMapSpecialAll);
			}else if("IR_AUTHORS".equals(column)) {			//增加微信库的类型
				List<SpecialWord> list=new LinkedList<>();
				XSSFWorkbook wb = new XSSFWorkbook();
				XSSFSheet sheet = wb.createSheet("疑似信息列表");
				XSSFRow header=sheet.createRow(0);
				header.createCell(0).setCellValue("IR_ID");
				header.createCell(1).setCellValue("链接地址");
				header.createCell(2).setCellValue("链接内容");  
				header.createCell(3).setCellValue("网站名称");
				header.createCell(4).setCellValue("组名");  
				header.createCell(5).setCellValue("检索词");
				//定义出异常的数量
				int bugNum=0;
				int q=0;
				for(int j=0; j<where.length; j++) {
					for(int i=0; i<filePath.length; i++) {
						list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);
						for(int z=1;z<list.size()+1;z++) {
							q=q+1;
							SpecialWord specialWord=list.get(z-1);
							XSSFRow header2=sheet.createRow(q);
							header2.createCell(0).setCellValue(specialWord.getId());
							header2.createCell(1).setCellValue(specialWord.getUrl());
							header2.createCell(2).setCellValue(specialWord.getContent());  
							header2.createCell(3).setCellValue(specialWord.getSiteName());
							header2.createCell(4).setCellValue(specialWord.getGroupname());  
							header2.createCell(5).setCellValue(specialWord.getKeyword());
						}
						bugNum=bugNum+list.size();
						System.out.println("");
					}
				}
				header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
				//设置列的宽度
				for(int i=0;i<header.getPhysicalNumberOfCells();i++){
					sheet.setColumnWidth(i, 255*20);
				}
				header.setHeightInPoints(30);
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream("d:/疑似信息分网站统计.xlsx");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				//向指定文件写入内容
				try {
					wb.write(fos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}else if("wangpan".equals(serverTable)) {
				List<WangPan> list=new LinkedList<>();
				XSSFWorkbook wb = new XSSFWorkbook();
				XSSFSheet sheet = wb.createSheet("疑似信息列表");
				XSSFRow header=sheet.createRow(0);
				header.createCell(0).setCellValue("ip");
				header.createCell(1).setCellValue("类型");
				header.createCell(2).setCellValue("文件地址");  
				header.createCell(3).setCellValue("检索词");
				for(int j=0; j<where.length; j++) {
					for(int i=0; i<filePath.length; i++) {
						list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+")",false);
						System.out.println("==============================================================================");
						//				System.out.println("以上为  " + serverTable + " 表的站点为" + where[j] + "在此文件的‘" + filePath[i] + "’敏感词范围内，共检索到 " + recordNum + "条记录");
						for(int z=0;z<list.size();z++) {
							WangPan wangPan=list.get(z);
							System.out.println("list的详细"+list.get(z).toString());
							XSSFRow header2=sheet.createRow(z+1);
							header2.createCell(0).setCellValue(wangPan.getIp());
							header2.createCell(1).setCellValue(wangPan.getType());
							header2.createCell(2).setCellValue(wangPan.getFilepath());  
							header2.createCell(3).setCellValue(wangPan.getKeyword());
						}

						System.out.println("");
					}
				}
				//设置列的宽度
				//getPhysicalNumberOfCells()代表这行有多少包含数据的列
				for(int i=0;i<header.getPhysicalNumberOfCells();i++){
					//POI设置列宽度时比较特殊，它的基本单位是1/255个字符大小，
					//因此我们要想让列能够盛的下20个字符的话，就需要用255*20
					sheet.setColumnWidth(i, 255*20);
				}
				//设置行高，行高的单位就是像素，因此30就是30像素的意思
				header.setHeightInPoints(30);
				//上面设置好了内容，我们当然是要输出到某个文件的，输出就需要有输出流
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream("d:/疑似信息总数.xlsx");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//向指定文件写入内容
				try {
					wb.write(fos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}else if("id".equals(column)) {
				List<AuthorModel> list=new LinkedList<>();
				XSSFWorkbook wb = new XSSFWorkbook();
				XSSFSheet sheet = wb.createSheet("疑似信息总数列表");
				XSSFRow header=sheet.createRow(0);
				header.createCell(0).setCellValue("作者id");
				header.createCell(1).setCellValue("作者名称");
				header.createCell(2).setCellValue("MID");  
				header.createCell(2).setCellValue("检索词");	  
				//定义出异常的数量
				int bugNum=0;
				int q=0;
				for(int j=0; j<where.length; j++) {
					for(int i=0; i<filePath.length; i++) {
						String str=where[j]+getSensitiveWords(filePath[i])+"%)";
						list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);

						if(list.size()>0) {
							AuthorModel authorModel=list.get(0);
							q=q+1;
							XSSFRow header2=sheet.createRow(q);
							header2.createCell(0).setCellValue(authorModel.getAuthor_id());
							header2.createCell(1).setCellValue(authorModel.getAuthor_name());
							header2.createCell(2).setCellValue(authorModel.getMid());  
							header2.createCell(3).setCellValue(authorModel.getKeyword());  
							header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, authorModel.getSip()));  
							header2.createCell(2).setCellValue(list.size());
							System.out.println("");
						}else {
							list = csw.searchMessage(serverTable,where[j]+""+"%)",false);
							AuthorModel authorModel=list.get(0);
							q=q+1;
							XSSFRow header2=sheet.createRow(q);
							header2.createCell(0).setCellValue(authorModel.getAuthor_id());
							header2.createCell(1).setCellValue(authorModel.getAuthor_name());
							header2.createCell(2).setCellValue(authorModel.getMid());  
							header2.createCell(3).setCellValue(authorModel.getKeyword());  
							header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable,authorModel.getSip()));  
							header2.createCell(2).setCellValue(0);
						}
						bugNum=bugNum+list.size();
						System.out.println("");
					}
				}
				header.createCell(6).setCellValue("记录数总量: "+CountTotalRecordNum.getDataNumAll(serverTable)+"   异常数量为:"+bugNum);
				//设置列的宽度
				for(int i=0;i<header.getPhysicalNumberOfCells();i++){
					sheet.setColumnWidth(i, 255*20);
				}
				header.setHeightInPoints(30);
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream("d:/疑似信息.xlsx");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				//向指定文件写入内容
				try {
					wb.write(fos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  

			}else if("Y_ID".equals(column)) {
				List<JCMS> list=new LinkedList<>();
				XSSFWorkbook wb = new XSSFWorkbook();
				XSSFSheet sheet = wb.createSheet("疑似信息总数列表");
				XSSFRow header=sheet.createRow(0);
				header.createCell(0).setCellValue("Y_ID");
				header.createCell(1).setCellValue("ip");
				header.createCell(2).setCellValue("SPORT");  
				header.createCell(3).setCellValue("数据库名称");  
				header.createCell(4).setCellValue("数据库类型");  
				header.createCell(5).setCellValue("检索词");  
				//定义出异常的数量
				int bugNum=0;
				int q=0;
				for(int j=0; j<where.length; j++) {
					for(int i=0; i<filePath.length; i++) {
						String str=where[j]+getSensitiveWords(filePath[i])+"%)";
						list = csw.searchMessage(serverTable,where[j]+getSensitiveWords(filePath[i])+"%)",false);

						if(list.size()>0) {
							q=q+1;
							JCMS jcms=list.get(0);
							XSSFRow header2=sheet.createRow(q);
							header2.createCell(0).setCellValue(jcms.getSID());
							header2.createCell(1).setCellValue(jcms.getSIP());
							header2.createCell(2).setCellValue(jcms.getSPORT());  
							header2.createCell(3).setCellValue(jcms.getTB_NAME());  
							header2.createCell(4).setCellValue(jcms.getDB_TYPE());
							header2.createCell(5).setCellValue(jcms.getKeyword()); 
							header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, jcms.getSIP()));  
							header2.createCell(2).setCellValue(list.size());
							System.out.println("");
						}else {
							list = csw.searchMessage(serverTable,where[j]+""+"%)",false);
							q=q+1;
							JCMS jcms=list.get(0);
							XSSFRow header2=sheet.createRow(q);
							header2.createCell(0).setCellValue(jcms.getSIP());
							header2.createCell(1).setCellValue(CountTotalRecordNum.getColumnNum(serverTable, jcms.getSIP()));  
							header2.createCell(2).setCellValue(0);
						}
						for(int z=1;z<list.size()+1;z++) {
							q=q+1;
							JCMS jcms=list.get(z-1);
							XSSFRow header2=sheet.createRow(q);
							header2.createCell(0).setCellValue(jcms.getSID());
							header2.createCell(1).setCellValue(jcms.getSIP());
							header2.createCell(2).setCellValue(jcms.getSPORT());  
							header2.createCell(3).setCellValue(jcms.getTB_NAME());  
							header2.createCell(4).setCellValue(jcms.getDB_TYPE());
							header2.createCell(5).setCellValue(jcms.getKeyword());  
						}
						bugNum=bugNum+list.size();
						System.out.println("");
					}
				}
			}
		}
	}
*/
