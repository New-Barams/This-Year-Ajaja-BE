package me.ajaja.module.feedback.application;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.feedback.domain.FeedbackQueryRepository;
import me.ajaja.module.feedback.infra.model.AchieveInfo;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadTotalAchieveService {
	private final FeedbackQueryRepository feedbackQueryRepository;

	public int loadTotalAchieveByUserId(Long userId, int year) {
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
