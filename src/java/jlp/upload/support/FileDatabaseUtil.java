package jlp.upload.support;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileDatabaseUtil {
	public static final int BUFSIZE = 1024 * 8;  
	
	FileDatabaseUtil() {
	}

	public static void main(String args[]) {
		FileUtil util=new FileUtil();
		try {
			util.mergePartFiles("F://file", "JAVAAPI_CN", 5242880, "F://test.zip");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public static Connection getconnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/sss?user=root&password=1234&useUnicode=true&characterEncoding=UTF-8";
			Connection conn = DriverManager.getConnection(url);
			return conn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 时间戳+两位随机数生成文件id
	 * 
	 * @return
	 */
	public static long getNextIdByTime() {
		String s = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String s1 = (int) (Math.random() * 90) + "";
		return Long.parseLong(s + s1);
	}

	/**
	 * 保存文件信息
	 * 
	 * @param file
	 * @return
	 */
	public static long saveFileInfo(FilePiece file) {
		if (0L == file.getFileId())
			file.setFileId(getNextIdByTime());
		PreparedStatement stm = null;
		try {
			String sql = "insert into sys_fileinfo (fileId,fileName, filePath,fileSize,fileType,chunk,chunks)"
					+ "values(?,?,?,?,?,?,?);";
			Connection conn = getconnect();
			conn.setTransactionIsolation(4);
			stm = conn.prepareStatement(sql);
			stm.setLong(1, file.getFileId());
			stm.setString(2, file.getFileName());
			stm.setString(3, file.getFilePath());
			stm.setLong(4, file.getFileSize());
			stm.setString(5, file.getFileType());
			stm.setInt(6, file.getChunk());
			stm.setInt(7, file.getChunks());
			stm.execute();
			stm.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file.getFileId();
	}

	/**
	 * 将带后缀的文件名拆分为文件名和后缀
	 */
	public static String[] divideFilename(String filename) {
		String[] divs = filename.split("\\.");
		int len = divs.length;
		if (len == 2) {
			return new String[] { divs[0], divs[1] };
		} else {
			String name = divs[0];
			for (int i = 1; i < len - 1; i++) {
				name += "." + divs[i];
			}
			return new String[] { name, divs[len - 1] };
		}
	}
	
	
	public static void mergeFiles(String outFile, String[] files) {  
        FileChannel outChannel = null;  
        try {  
            outChannel = new FileOutputStream(outFile).getChannel();  
            for(String f : files){  
                FileChannel fc = new FileInputStream(f).getChannel();   
                ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);  
                while(fc.read(bb) != -1){  
                    bb.flip();  
                    outChannel.write(bb);  
                    bb.clear();  
                }  
                fc.close();  
            }  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
        } finally {  
            try {if (outChannel != null) {outChannel.close();}} catch (IOException ignore) {}  
        }  
    } 

	
	
}
