package jlp.upload.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jlp.upload.support.UploadPieces;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class UploadServlet
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static HashMap<String,UploadPieces> uploadMap=new HashMap<String,UploadPieces>();
	public static String uploadPath = "F:\\temp"; // 上传文件的目录
    private String tempPath = "F:\\temp\\buffer"; // 临时文件目录
    File tempPathFile=new File(tempPath);
    Map<String,String> paramMap=new HashMap<String,String>();
    FileItem toupload=null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(4096*1024*1024); // 设置缓冲区大小，这里是4kb
        factory.setRepository(tempPathFile);// 设置缓冲区目录
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        // Set overall request size constraint
        upload.setSizeMax(4194304*1021); // 设置最大文件尺寸，这里是4MB
        
        UP(request,upload);
        PiecesCombine();
        response.setContentType("json");
        PrintWriter out=response.getWriter();
        out.write("{a:'upload succeed'}");
        
	}

	
	
	@Override
	public void destroy() {
		super.destroy();
	}
	public void PiecesCombine(){
		String name=paramMap.get("name");
		UploadPieces up=uploadMap.get(name);
		if(null!=up){
			if(up.isFull())
			{
				try{
				up.combine();
				}catch(Exception e){
					
				}finally{
					uploadMap.remove(name);
				}
			}
		}
	}
	
	public void UP(HttpServletRequest request,ServletFileUpload upload){
	List<FileItem> items;
		try {
			items = upload.parseRequest(request);
	    	for(int i=0;i<items.size();i++){
	    		if(items.get(i).isFormField())
	    			paramMap.put(items.get(i).getFieldName(), items.get(i).getString("UTF-8"));
	    		else
	    			toupload=items.get(i);
	    	}
	    	String name=paramMap.get("name");
	    	UploadPieces up=uploadMap.get(name);
	    	initUp(name);
	    	if(null!=up)
	    		up.uploadPiece(paramMap,toupload);
		} catch (FileUploadException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	
}

	public synchronized void initUp(String name){
		UploadPieces up=uploadMap.get(name);
		if(null==up){
    		//System.out.println(0);
			UploadPieces up1=new UploadPieces(paramMap,toupload);
    		uploadMap.put(name, up1);
    		//up.uploadPiece(paramMap,toupload);
    	}
	}
	
}
