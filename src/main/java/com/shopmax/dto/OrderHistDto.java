package com.shopmax.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.shopmax.constant.OrderStatus;
import com.shopmax.entity.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderHistDto {
	
	//엔티티를 디티오로 변환
	public OrderHistDto(Order order) {
		this.orderId = order.getId();
		this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		this.orderStatus = order.getOrderStatus();
	}
	
	
	private Long orderId; //주문아이디
	
	private String orderDate; //주문날짜
	
	private OrderStatus orderStatus; //주문 상태
	
	private List<OrderItemDto> orderItemDtoList = new ArrayList<>(); // 주문상품 리스트
	
	
	//orderItemDto 객체를 주문 상품 리스트에 추가하는 메소드
	public void addOrderItemDto(OrderItemDto orderItemDto) {
		this.orderItemDtoList.add(orderItemDto);
	}
	
}
