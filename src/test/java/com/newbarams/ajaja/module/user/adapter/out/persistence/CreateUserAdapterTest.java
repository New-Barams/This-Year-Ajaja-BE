package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.newbarams.ajaja.common.support.JpaTestSupport;
import com.newbarams.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.mapper.UserMapperImpl;

@ContextConfiguration(classes = {
	CreateUserAdapter.class,
	UserMapperImpl.class
})
class CreateUserAdapterTest extends JpaTestSupport {
	@Autowired
	private CreateUserAdapter createUserAdapter;
	@Autowired
	private UserJpaRepository userJpaRepository;

	@Test
	void create_Success() {
		// given
		String phoneNumber = "01012345678";
		String email = "ajaja@me.com";

		User user = User.init(1L, "+82 1012345678", "ajaja@me.com");

		// when
		Long userId = createUserAdapter.create(user);

		// then
		assertThat(userId).isNotNull();

		List<UserEntity> entities = userJpaRepository.findAll();
		assertThat(entities).isNotEmpty();

		UserEntity userEntity = entities.get(0);
		assertThat(userEntity.getPhoneNumber()).isEqualTo(phoneNumber);
		assertThat(userEntity.getSignUpEmail()).isEqualTo(email);
		assertThat(userEntity.getRemindType()).isEqualTo("KAKAO");
		assertThat(userEntity.isDeleted()).isFalse();
	}
}
