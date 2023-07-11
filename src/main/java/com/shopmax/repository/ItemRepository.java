package com.shopmax.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.shopmax.constant.ItemSellStatus;
import com.shopmax.entity.Item;

// 해당 jpaRepositotn(에서 사용할 , entity클래스의 기본키 타입
public interface ItemRepository  extends JpaRepository<Item, Long> , ItemRepositoryCustom{
	//select * from item where item_nm = ?
	List<Item> findByItemNm(String ItemNm);
	
	
	//select * from item where item_nm = ? and item_sell_status = ?
	List<Item> findByItemNmAndItemSellStatus(String ItemNm, ItemSellStatus itemSellStatus);
	
	
	List<Item> findByPriceBetween(int price, int price2);
	
	List<Item> findByRegTimeAfter(LocalDateTime regtime);
	
	List<Item> findByItemSellStatusNotNull();
	
	List<Item> findByItemDetailLike(String ItemDetail);
	
	List<Item> findByItemNmLikeOrItemDetailLike(String ItemNm, String ItemDetail);
	
	List<Item> findByPriceLessThanOrderByPriceDesc(int price);
	
	
	//JPQL(non native 쿼리) -> 컬럼명, 테이블명은 entity 클래스 기준으로 작성한다
	@Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
	List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
	
	@Query(value="select * from item where item_detail like %:itemDetail% order by price desc", nativeQuery = true)
	List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
	
	
	
	@Query("select i from Item i where i.price >= :price")
	List<Item> findByItemPrice(@Param("price")  int price);
	
	
	@Query("select i from Item i where i.itemNm like %:itemNm% and i.itemSellStatus = :sell")
	List<Item> findByItemNmAndItemSellStatus2(@Param("itemNm") String itemNm,@Param("sell") ItemSellStatus sell);
	
	
	
	

}
