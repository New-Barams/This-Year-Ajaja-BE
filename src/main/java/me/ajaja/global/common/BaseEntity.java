package me.ajaja.global.common;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<T> extends SelfValidating<T> {
	@CreatedDate
	@Column(updatable = false)
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;
}
