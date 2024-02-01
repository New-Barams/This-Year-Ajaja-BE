package me.ajaja.module.user.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import me.ajaja.common.support.JpaTestSupport;
import me.ajaja.module.user.adapter.out.persistence.model.OauthInfo;
import me.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import me.ajaja.module.user.dto.UserResponse;

@ContextConfiguration(classes = GetMyPageAdapter.class)
class GetMyPageAdapterTest extends JpaTestSupport {
	@Autowired
	private GetMyPageAdapter getMyPageAdapter;
	@Autowired
	private UserJpaRepository userJpaRepository;

	private UserEntity user;

	@BeforeEach
	void setup() {
		user = userJpaRepository.save(new UserEntity(
			null,
			"nickname",
			"01012345678",
			"email",
			"email",
			false,
			"type",
			OauthInfo.kakao(sut.giveMeOne(Long.class)),
			false
		));
	}

	@Test
	@DisplayName("사용자의 정보를 불러오면 올바른 응답값으로 가져올 수 있어야 한다.")
	void findUserInfoById_Success() {
		// given
		Long id = user.getId();

		// when
		UserResponse.MyPage result = getMyPageAdapter.findUserInfoById(id);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getDefaultEmail()).isEqualTo(user.getSignUpEmail());
		assertThat(result.getRemindEmail()).isEqualTo(user.getRemindEmail());
		assertThat(result.isEmailVerified()).isEqualTo(user.isVerified());
		assertThat(result.getReceiveType()).isLowerCase();
	}

	@Test
	@DisplayName("존재하지 않는 사용자는 null을 리턴한다.")
	void findUserInfoById_Fail_ByNotExists() {
		// given
		Long userId = -1L;

		// when
		UserResponse.MyPage result = getMyPageAdapter.findUserInfoById(userId);

		// then
		assertThat(result).isNull();
	}
}
