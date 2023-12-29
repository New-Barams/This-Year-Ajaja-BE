package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.newbarams.ajaja.common.support.JpaTestSupport;
import com.newbarams.ajaja.module.user.infra.OauthInfo;
import com.newbarams.ajaja.module.user.infra.UserEntity;
import com.newbarams.ajaja.module.user.infra.UserJpaRepository;

@ContextConfiguration(classes = DeleteUserAdapter.class)
class DeleteUserAdapterTest extends JpaTestSupport {
	@Autowired
	private DeleteUserAdapter deleteUserAdapter;
	@Autowired
	private UserJpaRepository userJpaRepository;

	@Test
	void delete_Success() {
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
		deleteUserAdapter.delete(entity.getId());

		// then
		List<UserEntity> entities = userJpaRepository.findAll();
		assertThat(entities).isEmpty();
	}
}
