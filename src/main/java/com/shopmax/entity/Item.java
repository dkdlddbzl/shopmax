package com.shopmax.entity;


import com.shopmax.constant.ItemSellStatus;
import com.shopmax.dto.ItemFormDto;
import com.shopmax.exception.OutOfStockException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity //엔티티 클래스로 정의
@Table(name="item") //테이블 이름 지정
@Getter
@Setter
@ToString
public class Item  extends BaseEntity{
	
	@Id
	@Column(name="item_id") //테이블로 생성될때 컬럼이름을 지정해준다
	@GeneratedValue(strategy = GenerationType.AUTO) //기본키를 자동으로 생성해주는 전략 사용
	private Long id; //상품코드
	
	
	@Column(nullable = false, length = 50)
	private String itemNm; //상품명   not  lull 여부, 컬럼 킉=
	
	@Column(nullable = false)
	private int price; //가격
	
	@Column(nullable = false)
	private int stockNumber; //재고수량
	
	@Lob // clob과 같은 큼타입이 문자타입으로 컬럼은 만든다
	@Column(nullable = false, columnDefinition = "longtext")
	private String itemDetail; //상품 상세설명
	
	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus; //판매상태(SELL, SOLD_OUT)
	
	//item entity 수정
	public void updateItem(ItemFormDto itemFormDto) {
		this.itemNm = itemFormDto.getItemNm();
		this.price = itemFormDto.getPrice();
		this.stockNumber = itemFormDto.getStockNumber();
		this.itemDetail = itemFormDto.getItemDetail();
		this.itemSellStatus = itemFormDto.getItemSellStatus();

	}
	
	//재고를 감소시키는 메소드
	public void removeStock(int stockNumber) {
		int restStock = this.stockNumber - stockNumber;//남은 재고수량
		
		if(restStock < 0 ) {
			throw new OutOfStockException("상품의 재고가 부족합니다. 현재 재고 수량 : " + this.stockNumber);
		}
		 
		this.stockNumber = restStock; //남은 재고수량 반영
	}
	
	//재고를 증가시키는 메소드
	public void addStock(int stockNumber) {
		this.stockNumber += stockNumber;
	}
	
	
	
	
}
