package com.shopmax.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;




@EntityListeners(value = {AuditingEntityListener.class}) 
@MappedSuperclass //부모 클래스를 상속받는 자식 클래스한테 매핑정보를 제공하기 위해
@Setter
@Getter
public abstract class BaseEntity  extends BaseTimeEntity{
	
	
	@CreatedBy
	@Column(updatable = false)
	private String createdBy; //등록자
	
	
	@LastModifiedBy
	private String modifiedBy; //수정자
	
}
