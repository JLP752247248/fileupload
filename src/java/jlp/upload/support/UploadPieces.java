package jlp.upload.support;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import jlp.upload.servlet.UploadServlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.MultipartStream.MalformedStreamException;

import com.mysql.jdbc.StringUtils;

public class UploadPieces{
	
	//原文件名
	private String fileName;
	//碎片列表
	private List<FilePiece> pieces=new ArrayList<FilePiece>();
	//分割大小
	private long divideSize;
	//上传地址
	private String uploadPath;
	//分割数
	private int divideNum;
	
	
	
	
	public UploadPieces(Map<String, String> paramMap,FileItem fi) {
		systemPrintInfo();
		System.out.println("正在初始化UploadPieces-----------------------");
			try {
				if(StringUtils.isEmptyOrWhitespaceOnly(paramMap.get("chunks")))
				{
					this.divideNum=1;
				}
				else{
					this.divideNum=Integer.parseInt(paramMap.get("dividNum"));
				}
				this.fileName=paramMap.get("name");
				this.uploadPath=UploadServlet.uploadPath;
				this.setDivideSize(Long.parseLong(paramMap.get("chunkSize")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		uploadPiece(paramMap, fi);
		System.out.println("初始化完毕！");
	}


	public boolean isSorted(){
		for(int i=0;i<pieces.size();i++){
			if(pieces.get(i).getChunk()!=i)
				return false;
		}
		return true;
	}
	
	
	public void addPiece(FilePiece fp){
		this.pieces.add(fp);
	}
	
	
	
	public boolean isFull(){
		if(divideNum==pieces.size())
			return true;
		else
			return false;
	}
	
	public void sort(){
		Collections.sort(pieces, new FileComparator());
	}
	
	
	private class FileComparator implements Comparator<File> {
        public int compare(File o1, File o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<FilePiece> getPieces() {
		return pieces;
	}

	public void setPieces(List<FilePiece> pieces) {
		this.pieces = pieces;
	}

	public long getDivideSize() {
		return divideSize;
	}

	public void setDivideSize(long divideSize) {
		this.divideSize = divideSize;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public int getDivideNum() {
		return divideNum;
	}

	public void setDivideNum(int divideNum) {
		this.divideNum = divideNum;
	}


	public void uploadPiece(Map<String, String> paramMap, FileItem toupload) {
		System.out.println("执行uploadPiece方法---------------------");
		int chunk=0;
		FilePiece fp;
		if(this.getDivideNum()!=1)
			fp=new FilePiece(new File(UploadServlet.uploadPath),this.fileName+"["+Integer.parseInt(paramMap.get("chunk"))+"].part");
		else
			fp=new FilePiece(new File(UploadServlet.uploadPath+"\\last"),this.fileName);
		fp.setChunk(chunk);
    	fp.setChunks(divideNum);
    	
    	try {
			toupload.write(fp);
			this.pieces.add(fp);
		}catch (MalformedStreamException e) {
			System.err.append("Stream ended unexpectedly");
		}catch(Exception e){
			e.printStackTrace();
		}
    	finally{
    		systemPrintInfo();
    	}
	}


	public void combine() {
		System.out.println("执行combine方法，判断是否分块-------------------------------");
		systemPrintInfo();
		if(this.getDivideNum()>1){
		FileUtil fileutil=new FileUtil();
		try {
			if(isFull())
				{
				System.out.println("分块完整，开始合并！");
				fileutil.mergePartFiles(uploadPath, this.fileName+"[", (int)this.divideSize,new File(uploadPath+"\\last",this.fileName).getPath());
				System.out.println("合并结束！");
				}
			else{
				throw new Exception("分块上传不完整！");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else{
			System.out.println("未分块，不用合并！");
		}
	}


	public void deletePieces() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(this.getDivideNum()>1){
		for(int i=0;i<pieces.size();i++){
			File file=new File(pieces.get(i).getPath());
				FileUtil.delete(file.getPath());
		}
		}
	}
	
	public void systemPrintInfo(){
		System.out.println("分块数:"+this.getDivideNum()+"当前已传分块数:"+this.pieces.size());
	}
	
}
