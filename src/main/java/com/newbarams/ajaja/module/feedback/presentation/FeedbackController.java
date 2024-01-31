package com.newbarams.ajaja.module.feedback.presentation;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.annotation.Authorization;
import com.newbarams.ajaja.global.security.annotation.UserId;
import com.newbarams.ajaja.module.feedback.application.LoadFeedbackInfoService;
import com.newbarams.ajaja.module.feedback.application.UpdateFeedbackService;
import com.newbarams.ajaja.module.feedback.dto.FeedbackRequest;
import com.newbarams.ajaja.module.feedback.dto.FeedbackResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class FeedbackController {

	private final UpdateFeedbackService updateFeedbackService;
	private final LoadFeedbackInfoService loadFeedbackInfoService;

	@Authorization
	@PostMapping("/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updateFeedback(
		@UserId Long userId,
		@PathVariable Long planId,
		@RequestBody FeedbackRequest.UpdateFeedback updateFeedback
	) {
		updateFeedbackService.updateFeedback(userId, planId, updateFeedback.getRate(), updateFeedback.getMessage());
		return AjajaResponse.ok();
	}

	@Authorization
	@GetMapping("/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<FeedbackResponse.FeedbackInfo> getFeedbackInfo(
		@UserId Long userId,
		@PathVariable Long planId
	) {
		FeedbackResponse.FeedbackInfo feedbackInfo = loadFeedbackInfoService.loadFeedbackInfoByPlanId(userId, planId);
		return AjajaResponse.ok(feedbackInfo);
	}
}
