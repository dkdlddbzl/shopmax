package com.shopmax.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.java.Log;


@Service
@Log
public class FileService {
	
	
	//파일 업로드 메소드
	public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
		UUID uuid = UUID.randomUUID();// 중복되지 않은 이름을 만든다
		
		
		//이미지1.jpg -> 이미지의 확장자 명을 구한다
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String savedFileName = uuid.toString() + extension; 
		String fileUploadFullurl = uploadPath + "/" + savedFileName;
		
		//파일 업로드
		FileOutputStream fos = new FileOutputStream(fileUploadFullurl);
		fos.write(fileData);
		fos.close();
		
		return savedFileName;
		
	}
	
	
	
	
	//파일  삭제	
	public void deleteFile(String filePath) throws  Exception {
		File deleteFile = new File(filePath); //파일이 저장된 경조를 이용해서 파일 객체 생성
		
		//파일삭제
		if(deleteFile.exists()) {
			deleteFile.delete();
			log.info("파일을 삭제하였습니다."); //로그기록을 저장한다
		} else {
			log.info("파일이 존재하지 않습니다.");
		}
	
	
	
	
}
}