package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static com.newbarams.ajaja.module.user.adapter.out.persistence.model.QUserEntity.*;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.user.application.port.out.GetMyPageQuery;
import com.newbarams.ajaja.module.user.dto.QUserResponse_MyPage;
import com.newbarams.ajaja.module.user.dto.UserResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
class GetMyPageAdapter implements GetMyPageQuery {
	private final JPAQueryFactory queryFactory;

	@Override
	public UserResponse.MyPage findUserInfoById(Long id) {
		return queryFactory.select(new QUserResponse_MyPage(
				userEntity.nickname,
				userEntity.signUpEmail,
				userEntity.remindEmail,
				userEntity.verified,
				userEntity.receiveType.stringValue().toLowerCase()))
			.from(userEntity)
			.where(userEntity.id.eq(id))
			.fetchOne();
	}
}
