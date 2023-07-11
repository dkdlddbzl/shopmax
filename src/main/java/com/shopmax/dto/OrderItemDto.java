package com.shopmax.dto;

import com.shopmax.entity.OrderItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
	
	//엔티티 -> Dto로 바꿔준다
	public OrderItemDto(OrderItem orderItem, String imgUrl) {
		this.itemNm = orderItem.getItem().getItemNm();
		this.count = orderItem.getCount();
		this.orderPrice = orderItem.getOrderPrice();
		this.imgUrl = imgUrl;
	}
	
	
	private String itemNm;
	
	private int count; 
	
	private int orderPrice;
	
	private String imgUrl;
	
}
