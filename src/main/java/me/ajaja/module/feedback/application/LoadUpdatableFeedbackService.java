package me.ajaja.module.feedback.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.BaseTime;
import me.ajaja.module.feedback.domain.FeedbackQueryRepository;
import me.ajaja.module.feedback.dto.FeedbackResponse;
import me.ajaja.module.feedback.mapper.FeedbackMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class LoadUpdatableFeedbackService {
	private final FeedbackQueryRepository feedbackQueryRepository;
	private final FindUpdatableTargetService findUpdatableTargetService;
	private final FeedbackMapper mapper;

	public List<FeedbackResponse.UpdatableFeedback> loadUpdatableFeedbacksByUserId(Long userId, BaseTime now) {
		return findUpdatableTargetService.findUpdatableTargetsByUserId(userId, now).stream()
			.filter(feedback -> !feedbackQueryRepository.existByPlanIdAndPeriod(feedback.planId(), feedback.deadLine()))
			.map(mapper::toResponse)
			.toList();
	}
}
