package com.enjoybt.common.util.linux;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  Class Name : CronTabUtil.java
 *  Description : Linux(CentOS)에서 사용되는 crontab 커맨드를 활용한 스케쥴러 관리 기능
 *  Modification Information
 * 
 *     수정일			수정자				수정내용
 *   ---------------------------------------------------
 *   2018. 3. 15.	장재호				최초 생성
 *
 *  @author 장재호
 *  @since 2018. 3. 15.
 *  @version 1.0
 * 
 *  Copyright (C) 2018 by ㈜제이비티 All right reserved.
 */
public class CronTabUtil {

	/* OS 정보 */
	private static final String OS = System.getProperty("os.name").toLowerCase();
	
	/* 로거  */
	private static final Logger LOGGER = LoggerFactory.getLogger(CronTabUtil.class);
	
	/**
	 * Description : OS의 정보가 Linux인지 확인하는 메서드 
	 * @return (Boolean)						- Linux 여부 
	 */
	private static boolean checkLinux() {
		return OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0;
	}
	
	/**
	 * Description : Crontab 설정을 삭제하는 기능
	 * @param cronFileDir(String)			- crontab을 관리하는 Directory 정보
	 * @param id(String)					- 삭제할 설정 ID
	 * @return(List<String>)				- 커맨스 실행 후 리턴된 데이터
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static List<String> removeCrontab(String cronFileDir, String id) throws IOException, InterruptedException {
		if(!checkLinux()) {
			throw new IOException("OS Error(Not Linux)");
		}
		
		File file = new File(cronFileDir, FilenameUtils.getName(id+".cron"));
		if(file.exists()) {
			file.delete();
		}
		return commitCrontab(cronFileDir);
	}
	
	/**
	 * Description : Crontab 설정 기능
	 * @param cronFileDir(String)			- crontab을 관리하는 Directory 정보
	 * @param id(String)					- 등록할 설정 ID
	 * @param cronExpres(String)			- cron 표현식
	 * @param command(String)				- 등록할 실행 커맨드
	 * @return(List<String>)				- 커맨스 실행 후 리턴된 데이터
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static List<String> setCrontab(String cronFileDir, String id, String cronExpres, String command) throws IOException, InterruptedException {
		if(!checkLinux()) {
			throw new IOException("OS Error(Not Linux)");
		}
		
		String expres = "* * * * *";
		if(!StringUtils.isEmpty(cronExpres)) {
			expres = cronExpres;
		}
		File file = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			
			file = new File(cronFileDir);
			if(!file.exists() || !file.isDirectory())  {
				file.mkdirs();
			}
			
			file = new File(cronFileDir, FilenameUtils.getName(id+".cron"));
			
			if(!file.getParentFile().isDirectory()) {
				throw new IOException("디렉터리(" + file.getParent() + ")가 잘못 지정되었습니다.");
			}
			
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(expres + "	" + command);
			bw.newLine();
		} catch (IOException e) {
			LOGGER.error("", e);
//			throw e;
		} finally {
			try{
				if(bw != null)
					bw.close();
			} catch(IOException e) {
				LOGGER.error("ERROR", e);
			}
			
			try{
				fw.close();
			} catch(IOException e) {
				LOGGER.error("ERROR", e);
			}
		}
		return commitCrontab(cronFileDir);
	}
	
	/**
	 * Description : 파일을 등록 후 실제 crontab 명령어를 통해서 스케쥴러 적용
	 * @param cronFileDir(String)			- crontab을 관리하는 Directory 정보
	 * @return(List<String>)				- 커맨스 실행 후 리턴된 데이터
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static List<String> commitCrontab(String cronFileDir) throws IOException, InterruptedException {
		File fileDir = new File(cronFileDir);
		List<String> commandLine = new ArrayList<String>();
		List<String> result = null;
		commandLine.add("cat");
		int cronCount = 0;
		if(fileDir.isDirectory()) {
			File[] files = fileDir.listFiles();
			if(files != null && files.length > 0) {
				for(File f : files) {
					if(f.getName().lastIndexOf(".cron") != -1) {
						cronCount++;
						commandLine.add(f.getPath());
					}else{
						LOGGER.debug("NO CronFile : " + f.getName());
					}
				}
			}
		}
		
		if(cronCount == 0) {
			Process p1 = Runtime.getRuntime().exec(new String[]{"crontab", "-r"});
			p1.waitFor();
			result = new ArrayList<String>();
			return result;
		}
		
		LOGGER.debug(commandLine.toString());
		
		ProcessBuilder builder = new ProcessBuilder(commandLine);
		Process p1 = builder.start();
		InputStream input = null;
		Process p2 = Runtime.getRuntime().exec(new String[]{"crontab", "-"});
		OutputStream output = null;
		try {
			input = p1.getInputStream();
			output = p2.getOutputStream();
			IOUtils.copy(input, output);
		} catch (IOException e) {
			LOGGER.error("IOException! ", e);
//			throw e;
		} finally {
			try {output.close();}catch(IOException e) {LOGGER.error("ERROR", e);}
			try {input.close(); }catch(IOException e) {LOGGER.error("ERROR", e);}
		}
		p2.waitFor();
		
		p2.destroy();
		p1.destroy();
		
		p1 = Runtime.getRuntime().exec(new String[]{"crontab", "-l"});
		InputStreamReader isr = new InputStreamReader(p1.getInputStream());
		try{
			result = IOUtils.readLines(isr);
		} catch(IOException e) {
			LOGGER.error("IOException! ", e);
		}finally {
			try {if(isr != null) isr.close(); } catch(IOException e1) {LOGGER.error("IOException ", e1);}
		}
		return result;
	}
}
