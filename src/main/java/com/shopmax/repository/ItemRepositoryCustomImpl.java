package com.shopmax.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shopmax.constant.ItemSellStatus;
import com.shopmax.dto.ItemSearchDto;
import com.shopmax.dto.MainItemDto;
import com.shopmax.dto.QMainItemDto;
import com.shopmax.entity.Item;
import com.shopmax.entity.QItem;
import com.shopmax.entity.QItemImg;

import jakarta.persistence.EntityManager;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

	private JPAQueryFactory queryFactory;
	
	public ItemRepositoryCustomImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	private BooleanExpression regDtsAfter(String searchDateType) {
		LocalDateTime dateTime = LocalDateTime.now(); 
		
		//현재 날짜로 부터 이전 날짜를 구해준다.
		if(StringUtils.equals("all", searchDateType) || searchDateType == null)  return null;
		else if(StringUtils.equals("1d", searchDateType)) dateTime = dateTime.minusDays(1); 
		else if(StringUtils.equals("1w", searchDateType)) dateTime = dateTime.minusWeeks(1);
		else if(StringUtils.equals("1m", searchDateType)) dateTime = dateTime.minusMonths(1);
		else if(StringUtils.equals("6m", searchDateType)) dateTime = dateTime.minusMonths(6);
		
		return QItem.item.regTime.after(dateTime); //이후의 시간
	}
	
	private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
		return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
	}
	
	
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		if(StringUtils.equals("itemNm", searchBy)) {
			return QItem.item.itemNm.like("%" + searchQuery + "%"); //itemNm LIKE %청바지%
		} else if(StringUtils.equals("createdBy", searchBy)) {
			return QItem.item.createdBy.like("%" + searchQuery + "%"); //createdBy LIKE %test.com%
		}
		
		return null;
	}
	
	
	@Override
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		List<Item> content = queryFactory
				.selectFrom(QItem.item) //select * from item
				.where(regDtsAfter(itemSearchDto.getSearchDateType()), // where reg_time = ?
					   searchSellStatusEq(itemSearchDto.getSearchItemSellStatus()), //and sell_status = ?
					   searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())) // and itemNm LIKE %검색어%
				.orderBy(QItem.item.id.desc())
				.offset(pageable.getOffset()) //데이터를 가져올 시작 index
				.limit(pageable.getPageSize()) //한번에 가지고 올 최대 개수
				.fetch();
		
		//https://querydsl.com/static/querydsl/4.1.0/apidocs/com/querydsl/core/types/dsl/Wildcard.html
		// Wildcard.count = count(*)
		long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchItemSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();
		
		
		return new PageImpl<>(content, pageable, total);
	}
	
    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }
    
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }
	
	
}
