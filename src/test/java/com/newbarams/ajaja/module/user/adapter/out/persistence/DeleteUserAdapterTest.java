package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.user.infra.UserEntity;
import com.newbarams.ajaja.module.user.infra.UserJpaRepository;

@SpringBootTest
@Transactional
class DeleteUserAdapterTest extends MockTestSupport {
	@Autowired
	private DeleteUserAdapter deleteUserAdapter;
	@Autowired
	private UserJpaRepository userJpaRepository;

	@Test
	void delete_Success() {
		// given
		UserEntity entity = userJpaRepository.save(sut.giveMeBuilder(UserEntity.class)
			.set("id", null)
			.set("deleted", false)
			.sample());

		// when
		deleteUserAdapter.delete(entity.getId());

		// then
		List<UserEntity> entities = userJpaRepository.findAll();
		assertThat(entities).isEmpty();
	}
}
