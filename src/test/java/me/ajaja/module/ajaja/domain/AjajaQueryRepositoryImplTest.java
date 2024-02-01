package me.ajaja.module.ajaja.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.module.remind.application.model.RemindableAjaja;

@SpringBootTest
@Transactional
class AjajaQueryRepositoryImplTest extends MockTestSupport {
	@Autowired
	private AjajaQueryRepository ajajaQueryRepository;

	@Test
	@DisplayName("해당 유저가 받은 아자자 개수들을 가져온다.")
	void findAjajaByUpdatedAt_Success_WithNpException() {
		// when
		List<RemindableAjaja> remindableAjajas = ajajaQueryRepository.findRemindableAjaja();

		// then
		assertThat(remindableAjajas).isEmpty();
	}
}
