package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.newbarams.ajaja.common.support.JpaTestSupport;
import com.newbarams.ajaja.module.remind.domain.PlanInfo;
import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.domain.UserInfo;
import com.newbarams.ajaja.module.remind.mapper.RemindMapper;
import com.newbarams.ajaja.module.remind.mapper.RemindMapperImpl;

@ContextConfiguration(classes = {
	SaveRemindAdapter.class,
	RemindMapperImpl.class
})
class SaveRemindAdapterTest extends JpaTestSupport {
	@Autowired
	private SaveRemindAdapter saveRemindAdapter;
	@Autowired
	private RemindJpaRepository remindJpaRepository;
	@Autowired
	private RemindMapper mapper;

	@Test
	@DisplayName("보낸 리마인드 정보를 저장한다.")
	void save_Success_WithNoException() {
		// given
		Remind remind = new Remind(new UserInfo(1L, null), new PlanInfo(1L, null), "화이팅", Remind.Type.PLAN, 2, 17);

		// when
		Remind save = saveRemindAdapter.save(remind);

		// then
		assertThat(save.getMessage()).isEqualTo(remind.getMessage());
	}
}
