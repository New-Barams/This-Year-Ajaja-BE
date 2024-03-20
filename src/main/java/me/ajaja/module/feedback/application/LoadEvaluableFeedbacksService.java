package me.ajaja.module.feedback.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.feedback.domain.FeedbackQueryRepository;
import me.ajaja.module.feedback.dto.FeedbackResponse;
import me.ajaja.module.feedback.mapper.FeedbackMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadEvaluableFeedbacksService {
	private final FeedbackQueryRepository feedbackQueryRepository;
	private final LoadEvaluableTargetsService loadEvaluableTargetsService;
	private final FeedbackMapper mapper;

	public List<FeedbackResponse.EvaluableFeedback> loadEvaluableFeedbacksByUserId(Long userId) {
		return loadEvaluableTargetsService.findEvaluableTargetsByUserId(userId).stream()
			.filter(feedback -> !feedbackQueryRepository.existByPlanIdAndPeriod(feedback.planId(), feedback.deadLine()))
			.map(mapper::toResponse)
			.toList();
	}
}
