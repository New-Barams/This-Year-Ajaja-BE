package me.ajaja.module.footprint.adapter.out.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import me.ajaja.common.support.JpaTestSupport;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.FootprintFactory;
import me.ajaja.module.footprint.dto.FootprintRequest;
import me.ajaja.module.footprint.mapper.FootprintMapperImpl;

@ContextConfiguration(classes = {
	CreateFootprintAdaptor.class,
	GetFootprintAdaptor.class,
	FootprintMapperImpl.class,
	FootprintFactory.class
})
class GetFootprintAdaptorTest extends JpaTestSupport {
	private final String userCreateQuery = """
		INSERT INTO users
		(deleted, verified, created_at, oauth_id, updated_at, user_id, phone_number, nickname, remind_type, email,
		remind_email, oauth_provider)
		VALUES
		(false, true, CURRENT_TIMESTAMP(6) AT TIME ZONE 'UTC', 1, CURRENT_TIMESTAMP(6) AT TIME ZONE 'UTC',
		DEFAULT, '12345678901', 'Example Nickname', 'example_remind_type',
		'example@email.com', 'example@remind.com', 'KAKAO');
		""";
	private final String planCreateQuery = """
		INSERT INTO plans
		(can_ajaja, can_remind, deleted, icon_number, is_public, remind_date, remind_term, remind_total_period,
		created_at, plan_id, updated_at, user_id, title, description, remind_time)
		VALUES
		(true, true, false, 1, true, 1, 7, 30, CURRENT_TIMESTAMP(6) AT TIME ZONE 'UTC', 1,
		CURRENT_TIMESTAMP(6) AT TIME ZONE 'UTC', 1, 'Example Plan', 'This is an example description.', '12:00 PM');
		""";

	@Autowired
	private CreateFootprintAdaptor createFootprintAdaptor;

	@Autowired
	private GetFootprintAdaptor getFootprintAdaptor;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private FootprintFactory footprintFactory;

	@BeforeEach
	void createTempDate() {
		jdbcTemplate.update(userCreateQuery);
		jdbcTemplate.update(planCreateQuery);
	}

	@Test
	@DisplayName("발자취 조회 매핑 기능 구현 테스트")
	void get_Footprint_Success() {
		// given
		Long userId = 1L;
		Long targetId = 1L;

		FootprintRequest.Create param = sut.giveMeBuilder(FootprintRequest.Create.class)
			.set("targetId", targetId)
			.set("title", "title")
			.set("type", Footprint.Type.FREE)
			.set("content", "content")
			.sample();

		Footprint freeFootprint = footprintFactory.create(userId, param);

		Long createdId = createFootprintAdaptor.create(freeFootprint);

		// when
		Footprint footprint = getFootprintAdaptor.getFootprint(createdId);

		// then
		assertAll(
			() -> assertThat(footprint.getId()).isEqualTo(createdId),
			() -> assertThat(footprint.getTarget().getId()).isEqualTo(targetId),
			() -> assertThat(footprint.getWriter().getId()).isEqualTo(userId),
			() -> assertThat(footprint.getType()).isEqualTo(Footprint.Type.FREE)
		);
	}
}
