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
import com.newbarams.ajaja.module.feedback.service.GetTotalAchieveService;
import com.newbarams.ajaja.module.feedback.service.UpdateFeedbackService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "feedback", description = "피드백 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class FeedbackController {

	private final UpdateFeedbackService updateFeedbackService;
	private final GetTotalAchieveService getTotalAchieveService;

	@ExceptionHandler(RuntimeException.class)
	public AjajaResponse<String> fakeExceptionHandler(RuntimeException exception) {
		return new AjajaResponse<>(false, exception.getMessage());
	}

	@Operation(summary = "피드백 반영 API")
	@PostMapping("/{feedbackId}")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updateFeedback(
		@PathVariable Long feedbackId,
		@RequestBody UpdateFeedback updateFeedback
	) throws IllegalAccessException {
		updateFeedbackService.updateFeedback(feedbackId, updateFeedback.rate());

		return new AjajaResponse<>(true, null);
	}

	@Operation(summary = "유저 전체 목표 달성률 조회 API")
	@GetMapping("/{userId}")
	@ResponseStatus(OK)
	public AjajaResponse<Integer> getTotalAchieve(
		@PathVariable Long userId
	) {
		int totalAchieve = getTotalAchieveService.calculateTotalAchieve(userId);

		return new AjajaResponse<>(true, totalAchieve);
	}
}
