package com.newbarams.ajaja.module.remind.adapter.out.feedback;

import org.springframework.stereotype.Service;

import com.newbarams.ajaja.module.feedback.application.LoadTotalAchieveService;
import com.newbarams.ajaja.module.remind.application.port.out.LoadTotalAchievePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoadTotalAchieveAdapter implements LoadTotalAchievePort {
	private final LoadTotalAchieveService loadTotalAchieveService;

	@Override
	public int load(Long userId, int year) {
		return loadTotalAchieveService.loadTotalAchieveByUserId(userId, year);
	}
}
