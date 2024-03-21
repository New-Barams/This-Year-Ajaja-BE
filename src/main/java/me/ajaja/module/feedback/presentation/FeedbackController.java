package me.ajaja.module.feedback.presentation;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.annotation.Login;
import me.ajaja.module.feedback.application.LoadFeedbackInfoService;
import me.ajaja.module.feedback.application.UpdateFeedbackService;
import me.ajaja.module.feedback.dto.FeedbackRequest;
import me.ajaja.module.feedback.dto.FeedbackResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class FeedbackController {
	private final UpdateFeedbackService updateFeedbackService;
	private final LoadFeedbackInfoService loadFeedbackInfoService;

	@Authorize
	@PostMapping("/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updateFeedback(
		@Login Long userId,
		@PathVariable Long planId,
		@RequestBody FeedbackRequest.UpdateFeedback updateFeedback
	) {
		updateFeedbackService.updateFeedback(userId, planId, updateFeedback.getRate(), updateFeedback.getMessage());
		return AjajaResponse.ok();
	}

	@Authorize
	@GetMapping("/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<FeedbackResponse.FeedbackInfo> getFeedbackInfo(
		@Login Long userId,
		@PathVariable Long planId
	) {
		FeedbackResponse.FeedbackInfo feedbackInfo = loadFeedbackInfoService.loadFeedbackInfoByPlanId(userId, planId);
		return AjajaResponse.ok(feedbackInfo);
	}
}
