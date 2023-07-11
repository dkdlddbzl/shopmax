package com.shopmax.dto;

import org.modelmapper.ModelMapper;

import com.shopmax.entity.ItemImg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImgDto {
	
	
	private Long id;
	
	private String imnName;
	
	private String oriImgName;
	
	private String imgUrl;
	
	private String repimgYn;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	//entity 를 Dto로 변환
	public static ItemImgDto of(ItemImg itemImg) {
		return modelMapper.map(itemImg, ItemImgDto.class);
	}
}
