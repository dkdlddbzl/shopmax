package com.shopmax.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.shopmax.entity.ItemImg;
import com.shopmax.repository.ItemImgRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
	
	
	
	
	private String itemImgLocation = "C:/shop/item";
	
	private final ItemImgRepository itemImgRepository;
	
	private final FileService fileService;
	
	
	//이미지를 저장 1.파일을  itemImgLocation에 저장
	
	public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
		String oriImgName = itemImgFile.getOriginalFilename(); //파일이름 - 이미지1.jpg
		String imgName = "";
		String imgUrl = "";
		
		if(!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			imgUrl = "/images/item/" + imgName; 
		}
		

		//2.item_img 테이블에 저장 -> 이미지1.jpg, ASDIJSADK.jpg, "") entity값을 update
		itemImg.updateItemImg(oriImgName, imgName, imgUrl);
		itemImgRepository.save(itemImg); //db에 insert
		
		
	}
	
	
	//이미지 업데이트 메소드
	public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
		if(!itemImgFile.isEmpty()) { //파일이 있으면
			ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
					.orElseThrow(EntityNotFoundException::new);
			
			//기존 이미지 파일 삭제
			if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
				// C:/shop/item/a110f979-1467-4c7e-8346-52373e55018d.jpg
				fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
			}
			
			//수정된 이미지 파일 업로드
			String oriImgName = itemImgFile.getOriginalFilename(); 
			String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			String imgUrl = "/images/item/" + imgName;
			
			//★ savedItemImg는 현재 영속상태이므로 데이터를 변경하는 것만으로 변경감지 기능이 동작하여 트랜잭션이 끝날때 update쿼리가 실행된다.
			//-> 엔티티가 반드시 영속상태여야 한다.
			//한번 insert를 진행하면 엔티티가 영속성 컨텍스트에 저장이 되므로
			// 그이후에는 변경감지 기능이 동작하기 때문에 개발자는 update쿼리문을 쓰지않고
			//엔티티만 변경해주면 된다
			savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
		}
	}
	
	
	
}
