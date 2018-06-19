package com.sbolo.syk.common.generator;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.DbKit;
import com.sbolo.syk.common.tools.DateUtil;  

public class GenerEntityUtil {  
      
    private String[] colnames; // 列名数组  
    private String[] colTypes; // 列名类型数组  
    private int[] colSizes; // 列名大小数组  
    private String[] commons;//注释数组
    private boolean[] isRequires;//是否必填数组
    private boolean f_util = false; // 是否需要导入包java.util.*  
    private boolean f_sql = false; // 是否需要导入包java.sql.*
    private boolean f_decimal = false;
    public static boolean isHaveGroupSearch = false; //是否需要高级搜索注解
    public static boolean isHaveExcel = false;	//是否需要excel注解(如果调用easyPoi到处excel则需要该注解)
    public static boolean isVo = false;		//是否是生成Vo的实体(生成Vo代码自动改变为true)
    
    public void generEntity(String packagePath, String tableName,String tableCnName,String voPackagePath) throws SQLException {  
    	//Db.use("test");
    	Connection conn = DbKit.getConfig("test").getConnection(); // 得到数据库连接  
//        PreparedStatement pstmt = null;  
//        String strsql = "select * from " + tableName;  
        try {  
//            pstmt = conn.prepareStatement(strsql);  
//            ResultSetMetaData rsmd = pstmt.getMetaData();  
//            DatabaseMetaData dbmd = conn.getMetaData(); 
//            ResultSet rs = dbmd.getTables(null, "%", tableName, new String[] { "TABLE" });
            Statement stmt = conn.createStatement();  
            ResultSet rs = stmt.executeQuery("show full columns from " + tableName);
            rs.last();
            Integer j = 0;
            int size =  rs.getRow(); // 共有多少列  
            rs.beforeFirst();
            //int size = 20;
            colnames = new String[size];  
            colTypes = new String[size];  
            colSizes = new int[size];  
            commons = new String[size];
            isRequires = new boolean[size];
            while(rs.next()){  
                System.out.println("字段名："+rs.getString("Field")+"--字段注释："+rs.getString("Comment")+"--字段数据类型：");  
                colnames[j] = rs.getString("Field");
                commons[j] = rs.getString("Comment");
                if(StringUtils.isBlank(commons[j])){
                	commons[j] = colnames[j];
                }
                isRequires[j] = rs.getString("Null").equals("NO")?true:false;
                String type = rs.getString("Type");
                Integer length = 0;
                if(type.indexOf("(") != -1){
                	colTypes[j] = type.substring(0, type.indexOf("(")); 
                	//length = Integer.parseInt(type.substring(type.indexOf("(")+1,type.indexOf(")")));
                }else{
                	colTypes[j] = type;
                }
            	 
                if (colTypes[j].equalsIgnoreCase("datetime")) {  
                    f_util = true;  
                }  
                if (colTypes[j].equalsIgnoreCase("image")  
                        || colTypes[j].equalsIgnoreCase("text")) {  
                    f_sql = true;  
                }  
                colSizes[j] = length;  
                j++;
            }  
           
//            for (int i = 0; i < rsmd.getColumnCount(); i++) {  
//                //colnames[i] = this.getCamelStr(rsmd.getColumnName(i + 1)); 
//            	System.out.println("getColumnLabel="+rsmd.getTableName(i+1));
//            	colnames[i] = rsmd.getColumnName(i + 1);
//            	colTypes[i] = rsmd.getColumnTypeName(i + 1);  
//                if (colTypes[i].equalsIgnoreCase("datetime")) {  
//                    f_util = true;  
//                }  
//                if (colTypes[i].equalsIgnoreCase("image")  
//                        || colTypes[i].equalsIgnoreCase("text")) {  
//                    f_sql = true;  
//                }  
//                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);  
//            }  
            try {
            	//复制Entity
            	isVo = false;
                String content = parse(colnames, colTypes, colSizes, packagePath, tableName,tableCnName,"Entity");  
                String path = System.getProperty("user.dir") + "/src/main/java/" + packagePath.replaceAll("\\.", "/");  
                File file = new File(path);  
                if(!file.exists()){  
                    file.mkdirs();  
                }  
                String resPath = path+"/"+initcap(tableName,true,true) + "Entity.java";  
                System.out.println("resPath=" + resPath);  
                FileUtils.writeStringToFile(new File(resPath), content);  
                
                //复制VO
                isVo = true;
                String newVoPackagePath = System.getProperty("user.dir") + "/src/main/java/" + voPackagePath.replaceAll("\\.", "/");
                String voPath = newVoPackagePath+"/"+initcap(tableName,true,true) + "VO.java";  
                System.out.println("resPath=" + voPath);  
                String content1 = parse(colnames, colTypes, colSizes, voPackagePath, tableName,tableCnName,"VO");
                FileUtils.writeStringToFile(new File(voPath), content1);  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        } finally {  
            DbKit.getConfig().close(conn); 
        }  
    }  
  
    /**  
    * 解析处理(生成实体类主体代码)  
    */  
    private String parse(String[] colNames, String[] colTypes, int[] colSizes, String packagePath, String tableName,String tableCnName,String suffix) {  
        StringBuffer sb = new StringBuffer();  
        sb.append("package " + packagePath + ";\r\n\r\n");  
        sb.append("import io.swagger.annotations.ApiModelProperty;\r\n");
        if(isHaveExcel && isVo){
        	sb.append("import cn.afterturn.easypoi.excel.annotation.Excel;\r\n");
        }
        if(isHaveGroupSearch && isVo){
        	sb.append("import com.sbolo.syk.common.annotation.GroupSearch;\r\n");
        }
        if (f_util) {  
            sb.append("import java.util.Date;\r\n");  
            sb.append("import org.springframework.format.annotation.DateTimeFormat;\r\n");
        }  
        if (f_sql) {  
            sb.append("import java.sql.*;\r\n\r\n\r\n");  
        }
        if(f_decimal){
        	sb.append("import java.math.BigDecimal;\r\n\r\n\r\n");
        }
        sb.append("\r\n");
        sb.append("/** \r\n * "+tableCnName+"\r\n * 创建时间:"+DateUtil.date2Str(new Date())+" \r\n */ \r\n");
        sb.append("public class " + initcap(tableName,true,true) + suffix +" {\r\n\r\n");  
        processAllAttrs(sb);  
        sb.append("\r\n");  
        processAllMethod(sb);  
        sb.append("}\r\n");  
        System.out.println(sb.toString());  
        return sb.toString();  
  
    }  
  
    /**  
    * 生成所有的方法  
    *   
    * @param sb  
    */  
    private void processAllMethod(StringBuffer sb) {  
        for (int i = 0; i < colnames.length; i++) {  
            sb.append("\tpublic void set" + initcap(colnames[i],false,false) + "("  
                    + sqlType2JavaType(colTypes[i]) + " " + colnames[i]  
                    + "){\r\n");  
            sb.append("\t\tthis." + colnames[i] + "=" + colnames[i] + ";\r\n");  
            sb.append("\t}\r\n\r\n");  
  
            sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get"  
                    + initcap(colnames[i],false,false) + "(){\r\n");  
            sb.append("\t\treturn " + colnames[i] + ";\r\n");  
            sb.append("\t}\r\n\r\n");  
        }  
    }  
  
    /**  
    * 解析输出属性  
    *   
    * @return  
    */  
    private void processAllAttrs(StringBuffer sb) {  
        for (int i = 0; i < colnames.length; i++) {  
        	System.out.println("colnames="+colnames[i]);
        	boolean needless = false;
        	if(colnames[i].equals("id")||colnames[i].equals("create_time")||colnames[i].equals("update_time")){
        		needless = true;
        	}
        	if(isHaveGroupSearch && isVo){
	         	sb.append("@GroupSearch(desc=\""+commons[i]+"\",val=\""+colnames[i]+"\")\r\n");
	        }
        	if(needless){
        		sb.append("\t@ApiModelProperty(value=\""+commons[i]+"\",dataType=\""+sqlType2JavaType(colTypes[i])+"\",required=false,hidden=true)\r\n");
        	}else{
        		sb.append("\t@ApiModelProperty(value=\""+commons[i]+"\",dataType=\""+sqlType2JavaType(colTypes[i])+"\",required="+isRequires[i]+")\r\n");
        	}
	    	 
        	//sb.append("\t@Excel(name=\""+commons[i]+"\",dataType=\""+sqlType2JavaType(colTypes[i])+"\",required="+isRequires[i]+")\r\n");
            if(sqlType2JavaType(colTypes[i]).equals("Date")){
            	if(isHaveExcel && isVo){
            		sb.append("\t@Excel(name=\""+commons[i]+"\",exportFormat=\"yyyy-MM-dd HH:mm:ss\")\r\n");
            	}
            	sb.append("\t@DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\r\n");
            }else{
            	if(isHaveExcel && isVo){
            		sb.append("\t@Excel(name=\""+commons[i]+"\")\r\n");
            	}
            }
        	sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + colnames[i] + ";\r\n");  
            sb.append("\r\n");
        }  
    }  
  
    /**  
    * 把输入字符串的首字母改成大写  
    *   
    * @param str  
    * @return  
    */  
    private String initcap(String str,boolean isCamel,boolean isTrim) {  
        char[] ch = str.toCharArray();  
        if (ch[0] >= 'a' && ch[0] <= 'z') {  
            ch[0] = (char) (ch[0]-32);  
        } 
        if(isCamel){
        	 return this.getCamelStr(new String(ch),isTrim); 
        }else{
        	return new String(ch);
        }
        
    } 
    
    //例：user_name --> userName  
    private String getCamelStr(String s,boolean isTrim){  
    	String[] temp = s.split("_");
    	String newStr = "";
    	for(int i=0;i<temp.length;i++){
    		if(isTrim&&i==0){
    			continue;
    		}
    		if(isTrim && i==temp.length-1 && temp[i].length() == 1){
    			continue;
    		}
    		newStr += temp[i].substring(0, 1).toUpperCase() + temp[i].substring(1,temp[i].length());
    	}
//        while(s.indexOf("_")>0){
//            int index = s.indexOf("_");  
//            //System.out.println(s.substring(index+1, index+2).toUpperCase());  
//            s = s.substring(0, index) + s.substring(index+1, index+2).toUpperCase() + s.substring(index+2);  
//            System.out.println(s);
//        }  
        return newStr;  
    }  
  
    private String sqlType2JavaType(String sqlType) {  
        if (sqlType.equalsIgnoreCase("bit")) {  
            return "Boolean";  
        } else if (sqlType.equalsIgnoreCase("tinyint")) {  
            return "Byte";  
        } else if (sqlType.equalsIgnoreCase("smallint")) {  
            return "Short";  
        } else if (sqlType.equalsIgnoreCase("int") || sqlType.equalsIgnoreCase("integer")) {  
            return "Integer";  
        } else if (sqlType.equalsIgnoreCase("bigint")) {  
            return "Long";  
        } else if (sqlType.equalsIgnoreCase("float")) {  
            return "Float";  
        } else if (sqlType.equalsIgnoreCase("decimal")  
                || sqlType.equalsIgnoreCase("numeric")  
                || sqlType.equalsIgnoreCase("real")) {
        	f_decimal = true;
            return "BigDecimal";  
        } else if (sqlType.equalsIgnoreCase("money")  
                || sqlType.equalsIgnoreCase("smallmoney")) {  
            return "Double";  
        } else if (sqlType.equalsIgnoreCase("varchar")  
                || sqlType.equalsIgnoreCase("char")  
                || sqlType.equalsIgnoreCase("nvarchar")  
                || sqlType.equalsIgnoreCase("nchar")|| sqlType.equalsIgnoreCase("text")|| sqlType.equalsIgnoreCase("longtext")) {  
            return "String";  
        } else if (sqlType.equalsIgnoreCase("datetime")||sqlType.equalsIgnoreCase("date")) {  
            return "Date";  
        } else if (sqlType.equalsIgnoreCase("image")) {  
            return "Blob";  
        } else if (sqlType.equalsIgnoreCase("text")) {  
            return "String";  
        } else if (sqlType.equalsIgnoreCase("double")) {
        	return "Double";
        }
        return null;  
    }  
      
}  