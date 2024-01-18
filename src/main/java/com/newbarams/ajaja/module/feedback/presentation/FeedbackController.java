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
import com.newbarams.ajaja.global.exception.ErrorResponse;
import com.newbarams.ajaja.module.feedback.application.LoadFeedbackInfoService;
import com.newbarams.ajaja.module.feedback.application.UpdateFeedbackService;
import com.newbarams.ajaja.module.feedback.dto.FeedbackRequest;
import com.newbarams.ajaja.module.feedback.dto.FeedbackResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "feedback", description = "피드백 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/feedbacks")
public class FeedbackController {

	private final UpdateFeedbackService updateFeedbackService;
	private final LoadFeedbackInfoService loadFeedbackInfoService;

	@Operation(summary = "[토큰 필요] 피드백 반영 API", description = "<b>평가할 피드백에 대한 id가 필요합니다.</b>",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공적으로 계획에 대한 정보를 불러왔습니다."),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "400", description = "피드백 기간이 아닙니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "계획 정보가 존재하지 않습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "409", description = "이미 평가된 피드백 정보가 있습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 문제입니다. 관리자에게 문의 바랍니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		})
	@PostMapping("/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updateFeedback(
		@PathVariable Long planId,
		@RequestBody FeedbackRequest.UpdateFeedback updateFeedback
	) {
		updateFeedbackService.updateFeedback(2L, planId, updateFeedback.getRate(), updateFeedback.getMessage());
		return AjajaResponse.ok();
	}

	@Operation(summary = "[토큰 필요] 피드백 정보 조회 API", description = "<b>피드백 계획에 대한 id가 필요합니다.</b>",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공적으로 피드백에 대한 정보를 불러왔습니다."),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "평가할 피드백에 대한 정보가 존재하지 않습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 문제입니다. 관리자에게 문의 바랍니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		})
	@GetMapping("/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<FeedbackResponse.FeedbackInfo> getFeedbackInfo(
		@PathVariable Long planId
	) {
		FeedbackResponse.FeedbackInfo feedbackInfo = loadFeedbackInfoService.loadFeedbackInfoByPlanId(1L, planId);
		return AjajaResponse.ok(feedbackInfo);
	}
}
