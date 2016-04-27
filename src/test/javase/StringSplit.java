package javase;

import java.io.File;
import java.util.ArrayList;

import jlp.upload.support.FileUtil;

public class StringSplit {

	public static void main(String[] args) {
		//String s="1234511781234";
		/*String s="what the fuck ! ? ,the . fuck |\\asdf";
		//String []result=s.split("[1-4]{2}",0);
		String []result=s.split("\\.|\\|",0);
		for(String s1:result){
			System.out.println(s1);
		}*/
		
		ArrayList<File> partFiles = FileUtil.getDirFiles("F:\\temp",
                "【ZXY】无套内射姐姐，妹妹在旁边玩手机（申精）.mp4[");
		partFiles=new FileUtil().sort(partFiles);
		System.out.println(partFiles);
		
	}

}
