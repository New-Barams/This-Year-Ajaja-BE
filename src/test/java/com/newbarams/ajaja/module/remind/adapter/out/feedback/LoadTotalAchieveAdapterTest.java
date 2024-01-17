package com.newbarams.ajaja.module.remind.adapter.out.feedback;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.feedback.application.LoadTotalAchieveService;

class LoadTotalAchieveAdapterTest extends MockTestSupport {
	@InjectMocks
	private LoadTotalAchieveAdapter loadTotalAchieveAdapter;

	@Mock
	private LoadTotalAchieveService loadTotalAchieveService;

	@Test
	@DisplayName("해당 유저id에 맞는 피드백 달성률 정보를 받는다.")
	void load_Success_WithNoException() {
		// given
		given(loadTotalAchieveService.loadTotalAchieveByUserId(anyLong(), anyInt())).willReturn(50);

		// when
		int achieve = loadTotalAchieveAdapter.load(1L, 2024);

		// then
		assertThat(achieve).isEqualTo(50);
	}
}
