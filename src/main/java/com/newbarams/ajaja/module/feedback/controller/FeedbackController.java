package com.newbarams.ajaja.module.feedback.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.module.feedback.domain.dto.UpdateFeedback;
import com.newbarams.ajaja.module.feedback.service.UpdateFeedbackService;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

	private final UpdateFeedbackService updateFeedbackService;

	public FeedbackController(UpdateFeedbackService updateFeedbackService) {
		this.updateFeedbackService = updateFeedbackService;
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
}
