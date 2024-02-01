package me.ajaja.module.user.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.remind.application.model.RemindAddress;
import me.ajaja.module.user.adapter.out.persistence.model.QUserEntity;
import me.ajaja.module.user.application.port.out.FindUserAddressPort;

@Repository
@RequiredArgsConstructor
public class FindUserAddressAdapter implements FindUserAddressPort {
	private final JPAQueryFactory queryFactory;

	@Override
	public RemindAddress findUserAddressByUserId(Long userId) {
		return queryFactory.select(Projections.constructor(RemindAddress.class,
				QUserEntity.userEntity.id,
				QUserEntity.userEntity.remindType,
				QUserEntity.userEntity.remindEmail,
				QUserEntity.userEntity.phoneNumber
			))
			.from(QUserEntity.userEntity)
			.where(QUserEntity.userEntity.id.eq(userId))
			.fetchFirst();
	}
}
