package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.user.infra.UserEntity;
import com.newbarams.ajaja.module.user.infra.UserJpaRepository;

class FindUserIdByEmailAdapterTest extends MockTestSupport {
	@InjectMocks
	private FindUserIdByEmailAdapter findUserIdByEmailAdapter;

	@Mock
	private UserJpaRepository userJpaRepository;

	private final String email = sut.giveMeOne(String.class);

	@Test
	@DisplayName("존재하는 사용자의 정보를 찾으면 반환값이 존재한다.")
	void findUserIdByEmail_Success_WithExistUser() {
		// given
		UserEntity userEntity = sut.giveMeOne(UserEntity.class);
		given(userJpaRepository.findBySignUpEmail(anyString())).willReturn(Optional.of(userEntity));

		// when
		Optional<Long> userId = findUserIdByEmailAdapter.findUserIdByEmail(email);

		// then
		assertThat(userId).isPresent();
		then(userJpaRepository).should(times(1)).findBySignUpEmail(anyString());
	}

	@Test
	@DisplayName("존재하지 않는 사용자의 정보를 찾으면 반환값이 없어야 한다.")
	void findUserIdByEmail_Fail_ByNotExistUser() {
		// given
		given(userJpaRepository.findBySignUpEmail(anyString())).willReturn(Optional.empty());

		// when
		Optional<Long> userId = findUserIdByEmailAdapter.findUserIdByEmail(email);

		// then
		assertThat(userId).isEmpty();
		then(userJpaRepository).should(times(1)).findBySignUpEmail(anyString());
	}
}
