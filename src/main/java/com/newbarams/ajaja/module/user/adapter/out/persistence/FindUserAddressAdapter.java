package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static com.newbarams.ajaja.module.user.adapter.out.persistence.model.QUserEntity.*;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.remind.application.model.RemindAddress;
import com.newbarams.ajaja.module.user.application.port.out.FindUserAddressPort;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FindUserAddressAdapter implements FindUserAddressPort {
	private final JPAQueryFactory queryFactory;

	@Override
	public RemindAddress findUserAddressByUserId(Long userId) {
		return queryFactory.select(Projections.constructor(RemindAddress.class,
				userEntity.signUpEmail,
				userEntity.receiveType,
				userEntity.remindEmail,
				userEntity.remindEmail
			))
			.from(userEntity)
			.fetchFirst();
	}
}
