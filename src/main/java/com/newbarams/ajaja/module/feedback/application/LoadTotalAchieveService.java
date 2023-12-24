package com.newbarams.ajaja.module.feedback.application;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.domain.FeedbackQueryRepository;
import com.newbarams.ajaja.module.feedback.infra.model.AchieveInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadTotalAchieveService {
	private final FeedbackQueryRepository feedbackQueryRepository;

	public int loadTotalAchieve(Long userId, int year) {
		List<AchieveInfo> achieves = feedbackQueryRepository.findAchievesByUserIdAndYear(userId, year);

		return (int)achieves.stream()
			.collect(Collectors.groupingBy(
				AchieveInfo::planId,
				averagingInt(AchieveInfo::achieve)
			)).values().stream().mapToDouble(Double::intValue)
			.average()
			.orElse(0);
	}
}
