package com.newbarams.ajaja.module.feedback.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.module.feedback.domain.dto.UpdateFeedback;
import com.newbarams.ajaja.module.feedback.service.GetPlanAchieveService;
import com.newbarams.ajaja.module.feedback.service.GetTotalAchieveService;
import com.newbarams.ajaja.module.feedback.service.UpdateFeedbackService;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

	private final UpdateFeedbackService updateFeedbackService;
	private final GetTotalAchieveService getTotalAchieveService;
	private final GetPlanAchieveService getPlanAchieveService;

	public FeedbackController(UpdateFeedbackService updateFeedbackService,
		GetTotalAchieveService getTotalAchieveService,
		GetPlanAchieveService getPlanAchieveService) {
		this.updateFeedbackService = updateFeedbackService;
		this.getTotalAchieveService = getTotalAchieveService;
		this.getPlanAchieveService = getPlanAchieveService;
	}

	@ExceptionHandler(RuntimeException.class)
	public AjajaResponse<String> fakeExceptionHandler(RuntimeException exception) {
		return new AjajaResponse<>(false, exception.getMessage());
	}

	@PostMapping("/{feedbackId}")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updateFeedback(
		@PathVariable Long feedbackId,
		@RequestBody UpdateFeedback updateFeedback
	) throws IllegalAccessException {
		updateFeedbackService.updateFeedback(feedbackId, updateFeedback.rate());

		return new AjajaResponse<>(true, null);
	}

	@GetMapping("/plan/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<Integer> getPlanAchieve(
		@PathVariable Long planId
	) {
		int totalAchieve = getPlanAchieveService.loadPlanAchieve(planId);

		return new AjajaResponse<>(true, totalAchieve);
	}

	@GetMapping("/{userId}")
	@ResponseStatus(OK)
	public AjajaResponse<Integer> getTotalAchieve(
		@PathVariable Long userId
	) {
		int totalAchieve = getTotalAchieveService.loadTotalAchieve(userId);

		return new AjajaResponse<>(true, totalAchieve);
	}
}
