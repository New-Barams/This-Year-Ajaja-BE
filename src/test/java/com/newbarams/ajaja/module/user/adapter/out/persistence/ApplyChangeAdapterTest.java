package com.newbarams.ajaja.module.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.newbarams.ajaja.common.support.JpaTestSupport;
import com.newbarams.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.mapper.UserMapper;
import com.newbarams.ajaja.module.user.mapper.UserMapperImpl;

@ContextConfiguration(classes = {
	ApplyChangeAdapter.class,
	UserMapperImpl.class
})
class ApplyChangeAdapterTest extends JpaTestSupport {
	@Autowired
	private ApplyChangeAdapter applyChangeAdapter;
	@Autowired
	private UserJpaRepository userJpaRepository;
	@Autowired
	private UserMapper userMapper;

	private User user;

	@BeforeEach
	void setup() {
		UserEntity entity = userMapper.toEntity(sut.giveMeBuilder(User.class)
			.set("email", new Email("ajaja@me.com"))
			.set("deleted", false)
			.sample());

		user = userMapper.toDomain(userJpaRepository.save(entity));
	}

	@ParameterizedTest
	@EnumSource(User.ReceiveType.class)
	@DisplayName("리마인드 타입 변경 요청이 정상적으로 반영되어야 한다.")
	void apply_Success_OnReceiveTypeChange(User.ReceiveType type) {
		// given
		user.updateReceive(type);

		// when
		applyChangeAdapter.apply(user);

		// then
		List<UserEntity> entities = userJpaRepository.findAll();
		assertThat(entities).isNotEmpty();

		UserEntity userEntity = entities.get(0);
		assertThat(userEntity.getReceiveType()).isEqualTo(type.name());
	}

	@RepeatedTest(3)
	@DisplayName("닉네임 수정 시 정상적으로 반영되어야 한다.")
	void apply_Success_OnNicknameChange() {
		// given
		user.updateNickname();
		String expect = user.getNickname().getNickname();

		// when
		applyChangeAdapter.apply(user);

		// then
		List<UserEntity> entities = userJpaRepository.findAll();
		assertThat(entities).isNotEmpty();

		UserEntity userEntity = entities.get(0);
		assertThat(userEntity.getNickname()).isEqualTo(expect);
	}

	@Test
	@DisplayName("삭제 시 정상적으로 반영되어야 한다.")
	void apply_Success_OnDelete() {
		// given
		user.delete();

		// when
		applyChangeAdapter.apply(user);

		// then
		List<UserEntity> entities = userJpaRepository.findAll();
		assertThat(entities).isEmpty();
	}
}
