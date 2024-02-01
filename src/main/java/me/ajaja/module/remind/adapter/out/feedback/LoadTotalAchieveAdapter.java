package me.ajaja.module.remind.adapter.out.feedback;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.feedback.application.LoadTotalAchieveService;
import me.ajaja.module.remind.application.port.out.LoadTotalAchievePort;

@Service
@RequiredArgsConstructor
public class LoadTotalAchieveAdapter implements LoadTotalAchievePort {
	private final LoadTotalAchieveService loadTotalAchieveService;

	@Override
	public int load(Long userId, int year) {
		return loadTotalAchieveService.loadTotalAchieveByUserId(userId, year);
	}
}
