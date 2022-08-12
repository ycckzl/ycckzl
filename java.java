package com.xiao.source.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class ReadBigFile {
	/*
	 * 将文件内容都载入到内存中进行读取，当遇到大文件时会出现OutOfMemoryError异常
	 */
	public void readFileFromMemory(String filePath) throws Exception{
		List<String> fileLines = FileUtils.readLines(new File(filePath), Charsets.UTF_8);
		for(String line : fileLines){
			System.out.println(line);
		}
	}
	/*
	 * 通过java.util.Scanner读取文件，不会将所有文件都载入到内存中
	 */
	public void readFileByScanner(String filePath) throws IOException{
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
			inputStream = new FileInputStream(filePath);
			sc = new Scanner(inputStream,"UTF-8");
			while(sc.hasNext()){
				String line = sc.next();
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			if(null != inputStream)
				inputStream.close();
			if(null != sc)
				sc.close();
		}
	}
	/*
	 * 通过org.apache.commons.io.LineIterator读取文件，不会将所有文件都载入到内存中
	 */
	public void readFileByCommonsIO(String filePath){
		LineIterator it = null;
		try {
			it = FileUtils.lineIterator(new File(filePath));
			while(it.hasNext()){
				String line = it.nextLine();
				System.out.println(line);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(null != it)
				LineIterator.closeQuietly(it);
		}
	}
	
	
	public static void main(String[] args) throws Exception{
		String filePath = "F:\\虚拟机.txt";
		ReadBigFile readBigFile = new ReadBigFile();
		//readBigFile.readFileFromMemory(filePath);
		//readBigFile.readFileByScanner(filePath);
		readBigFile.readFileByCommonsIO(filePath);
	}

}