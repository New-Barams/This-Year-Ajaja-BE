package com.newbarams.ajaja.module.feedback.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// @DataJpaTest
class FeedbackRepositoryTest {
	// @Autowired
	// FeedbackRepository feedbackRepository;

	// private final FixtureMonkey sut = FixtureMonkey.builder()
	// 	.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
	// 	.plugin(new JakartaValidationPlugin())
	// 	.build();

	@Test
	@DisplayName("유저에게 저장된 피드백들을 불러온다.")
	void findTotalAchieve_Success_withNoException() {
		// given
		// Feedback feedback1 = sut.giveMeBuilder(Feedback.class).set("userId", 1L).sample();
		// Feedback feedback2 = sut.giveMeBuilder(Feedback.class).set("userId", 2L).sample();
		//
		// feedbackRepository.saveAll(List.of(feedback1, feedback2));

		// when

		// then

	}

	@Test
	@DisplayName("삭제된 피드백들은 불러오지 않는다.")
	void findTotalAchieve_Fail_ByException() {
		// given

		// when

		// then

	}
}
