package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.newbarams.ajaja.common.support.JpaTestSupport;
import com.newbarams.ajaja.module.user.infra.OauthInfo;
import com.newbarams.ajaja.module.user.infra.UserEntity;
import com.newbarams.ajaja.module.user.infra.UserJpaRepository;

@ContextConfiguration(classes = ChangeReceiveTypeAdapter.class)
class ChangeReceiveTypeAdapterTest extends JpaTestSupport {
	@Autowired
	private ChangeReceiveTypeAdapter changeReceiveTypeAdapter;
	@Autowired
	private UserJpaRepository userJpaRepository;

	@ParameterizedTest
	@ValueSource(strings = {"KAKAO", "EMAIL", "BOTH"})
	void change_Success(String type) {
		// given
		UserEntity entity = userJpaRepository.save(new UserEntity(
			null,
			"nickname",
			"email",
			"email",
			false,
			"type",
			OauthInfo.kakao(sut.giveMeOne(Long.class)),
			false
		));

		// when
		changeReceiveTypeAdapter.change(entity.getId(), type);

		// then
		UserEntity saved = userJpaRepository.findAll().get(0);
		assertThat(saved.getReceiveType()).isEqualTo(type);
	}
}
