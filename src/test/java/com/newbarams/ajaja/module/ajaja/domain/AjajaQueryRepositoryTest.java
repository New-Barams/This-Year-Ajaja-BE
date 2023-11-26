package com.newbarams.ajaja.module.ajaja.domain;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.module.ajaja.domain.repository.AjajaQueryRepository;
import com.querydsl.core.Tuple;

@SpringBootTest
@Transactional
class AjajaQueryRepositoryTest extends MockTestSupport {
	@Autowired
	private AjajaQueryRepository ajajaQueryRepository;

	@Test
	@DisplayName("해당 유저가 받은 아자자 개수들을 가져온다.")
	void findAjajaByUpdatedAt_Success_WithNpException() {
		// when
		List<Tuple> remindableAjaja = ajajaQueryRepository.findRemindableAjaja();

		// then
		Assertions.assertThat(remindableAjaja.size()).isZero();
	}
}
