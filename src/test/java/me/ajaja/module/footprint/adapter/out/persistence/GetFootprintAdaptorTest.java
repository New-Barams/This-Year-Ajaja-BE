// package me.ajaja.module.footprint.adapter.out.persistence;
//
// import static org.assertj.core.api.AssertionsForClassTypes.*;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.test.context.ContextConfiguration;
//
// import me.ajaja.common.support.JpaTestSupport;
// import me.ajaja.module.footprint.domain.Footprint;
// import me.ajaja.module.footprint.domain.FootprintFactory;
// import me.ajaja.module.footprint.domain.FreeFootprint;
// import me.ajaja.module.footprint.domain.KptFootprint;
// import me.ajaja.module.footprint.domain.Target;
// import me.ajaja.module.footprint.domain.Writer;
// import me.ajaja.module.footprint.dto.FootprintParam;
// import me.ajaja.module.footprint.mapper.FootprintMapperImpl;
//
// @ContextConfiguration(classes = {
// 	CreateFootprintAdaptor.class,
// 	GetFootprintAdaptor.class,
// 	FootprintMapperImpl.class
// })
// class GetFootprintAdaptorTest extends JpaTestSupport {
// 	private final String userCreateQuery = """
// 			INSERT INTO users
// 			(deleted, verified, created_at, oauth_id, updated_at, user_id, phone_number, nickname, remind_type, email,
// 			 remind_email, oauth_provider)
// 			VALUES
// 			(false, true, CURRENT_TIMESTAMP(6) AT TIME ZONE 'UTC', 1, CURRENT_TIMESTAMP(6) AT TIME ZONE 'UTC',
// 			DEFAULT, '12345678901', 'example_nickname', 'example_remind_type', 'example@email.com', 'example@remind.com', 'KAKAO');
// 		""";
// 	private final String planCreateQuery = """
// 			INSERT INTO plans
// 			(can_ajaja, can_remind, deleted, icon_number, is_public, remind_date, remind_term, remind_total_period,
// 			created_at, plan_id, updated_at, user_id, title, description, remind_time)
// 			VALUES
// 			(true, true, false, 1, true, 1, 7, 30, CURRENT_TIMESTAMP(6) AT TIME ZONE 'UTC', DEFAULT,
// 			CURRENT_TIMESTAMP(6) AT TIME ZONE 'UTC', 1, 'Example Plan', 'This is an example description.', '12:00 PM');
// 		""";
//
// 	@Autowired
// 	private CreateFootprintAdaptor createFootprintAdaptor;
//
// 	@Autowired
// 	private GetFootprintAdaptor getFootprintAdaptor;
//
// 	@Autowired
// 	private JdbcTemplate jdbcTemplate;
//
// 	@BeforeEach
// 	void createTempDate() {
// 		jdbcTemplate.update(userCreateQuery);
// 		jdbcTemplate.update(planCreateQuery);
// 	}
//
// 	@Test
// 	@DisplayName("자유 형식 발자취 조회 매핑 기능 구현 테스트")
// 	void get_FreeFootprint_Success() {
// 		// given
// 		FootprintParam.Create param = sut.giveMeBuilder(FootprintParam.Create.class)
// 			.set("writer", new Writer(1L, "nickName"))
// 			.set("target", new Target(1L, "title"))
// 			.sample();
//
// 		String content = "content";
// 		FreeFootprint freeFootprint = FootprintFactory.freeTemplate(param, content);
//
// 		Long createdId = createFootprintAdaptor.create(freeFootprint);
//
// 		// when
// 		Footprint footprint = getFootprintAdaptor.getFootprint(createdId);
//
// 		// then
// 		assertThat(footprint.getId()).isEqualTo(createdId);
// 	}
//
// 	@Test
// 	@DisplayName("Kpt 형식 발자취 조회 매핑 기능 구현 테스트")
// 	void get_KptFootprint_Success() {
// 		// given
// 		FootprintParam.Create param = sut.giveMeBuilder(FootprintParam.Create.class)
// 			.set("writer", new Writer(1L, "nickName"))
// 			.set("target", new Target(1L, "title"))
// 			.sample();
//
// 		String keepContent = "keepContent";
// 		String problemContent = "problemContent";
// 		String tryContent = "tryContent";
// 		KptFootprint kptFootprint = FootprintFactory.kptTemplate(param, keepContent, problemContent, tryContent);
//
// 		Long createdId = createFootprintAdaptor.create(kptFootprint);
//
// 		// when
// 		Footprint footprint = getFootprintAdaptor.getFootprint(createdId);
//
// 		// then
// 		assertThat(footprint.getId()).isEqualTo(createdId);
// 	}
// }
