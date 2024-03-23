package me.ajaja.module.feedback.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.BaseTime;
import me.ajaja.module.feedback.domain.Feedback;
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
		List<Feedback> feedbacks = feedbackQueryRepository.findAllFeedbacksByUserId(userId);

		return loadEvaluableTargetsService.findEvaluableTargetsByUserId(userId).stream()
			.filter(feedback -> isNotEvaluate(feedbacks, feedback.planId(), feedback.period()))
			.map(mapper::toResponse)
			.toList();
	}

	private boolean isNotEvaluate(List<Feedback> feedbacks, Long planId, BaseTime period) {
		return feedbacks.stream().noneMatch(feedback ->
			feedback.getPlanId().equals(planId) && period.isWithinAMonth(feedback.getCreatedAt()));
	}
}
