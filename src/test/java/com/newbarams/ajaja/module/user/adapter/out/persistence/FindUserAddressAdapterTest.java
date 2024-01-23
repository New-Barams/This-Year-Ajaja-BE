package com.newbarams.ajaja.module.user.adapter.out.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.newbarams.ajaja.common.support.JpaTestSupport;
import com.newbarams.ajaja.module.remind.application.model.RemindAddress;
import com.newbarams.ajaja.module.user.adapter.out.persistence.model.OauthInfo;
import com.newbarams.ajaja.module.user.adapter.out.persistence.model.UserEntity;

@ContextConfiguration(classes = FindUserAddressAdapter.class)
class FindUserAddressAdapterTest extends JpaTestSupport {
	@Autowired
	private FindUserAddressAdapter findUserAddressAdapter;
	@Autowired
	private UserJpaRepository userJpaRepository;

	private UserEntity user;

	@Test
	@DisplayName("요청한 유저에게 전송할 리마인드 주소 정보를 가져온다.")
	void findUserAddressByUserId_Success_WithNoException() {
		// given
		user = userJpaRepository.save(new UserEntity(
			null,
			"nickname",
			"email",
			"email",
			false,
			"EMAIL",
			OauthInfo.kakao(sut.giveMeOne(Long.class)),
			false
		));

		Long id = user.getId();

		// when
		RemindAddress address = findUserAddressAdapter.findUserAddressByUserId(id);

		// then
		Assertions.assertThat(address.type()).isEqualTo("EMAIL");
		Assertions.assertThat(address.userEmail()).isEqualTo("email");
	}
}
