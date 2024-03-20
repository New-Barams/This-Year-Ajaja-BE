package me.ajaja.module.feedback.presentation;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.feedback.application.LoadFeedbackInfoService;
import me.ajaja.module.feedback.application.LoadUpdatableFeedbackService;
import me.ajaja.module.feedback.application.UpdateFeedbackService;
import me.ajaja.module.feedback.dto.FeedbackRequest;
import me.ajaja.module.feedback.dto.FeedbackResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class FeedbackController {
	private final UpdateFeedbackService updateFeedbackService;
	private final LoadFeedbackInfoService loadFeedbackInfoService;
	private final LoadUpdatableFeedbackService loadUpdatableFeedbackService;

	@Authorization
	@PostMapping("/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updateFeedback(
		@PathVariable Long planId,
		@RequestBody FeedbackRequest.UpdateFeedback updateFeedback
	) {
		Long userId = SecurityUtil.getUserId();
		updateFeedbackService.updateFeedback(userId, planId, updateFeedback.getRate(), updateFeedback.getMessage());
		return AjajaResponse.ok();
	}

	@Authorization
	@GetMapping("/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<FeedbackResponse.FeedbackInfo> getFeedbackInfo(@PathVariable Long planId) {
		Long userId = SecurityUtil.getUserId();
		FeedbackResponse.FeedbackInfo feedbackInfo = loadFeedbackInfoService.loadFeedbackInfoByPlanId(userId, planId);
		return AjajaResponse.ok(feedbackInfo);
	}

	@Authorization
	@GetMapping("/updatable")
	@ResponseStatus(OK)
	public AjajaResponse<List<FeedbackResponse.UpdatableFeedback>> getUpdatableFeedbacks() {
		Long userId = SecurityUtil.getUserId();
		List<FeedbackResponse.UpdatableFeedback> feedbacks
			= loadUpdatableFeedbackService.loadUpdatableFeedbacksByUserId(userId);
		return AjajaResponse.ok(feedbacks);
	}
}
