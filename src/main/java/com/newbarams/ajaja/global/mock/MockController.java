package com.newbarams.ajaja.global.mock;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.exception.ErrorResponse;
import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.global.security.jwt.util.JwtParser;
import com.newbarams.ajaja.global.security.jwt.util.JwtRemover;
import com.newbarams.ajaja.global.security.jwt.util.JwtValidator;
import com.newbarams.ajaja.module.ajaja.application.SchedulingAjajaRemindService;
import com.newbarams.ajaja.module.auth.dto.AuthRequest;
import com.newbarams.ajaja.module.auth.dto.AuthResponse;
import com.newbarams.ajaja.module.feedback.dto.FeedbackRequest;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.remind.application.SchedulingRemindService;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "mock", description = "가짜 API")
@RestController
@RequestMapping("/mock")
@RequiredArgsConstructor
public class MockController {
	private static final String CERTIFICATION = "123456";
	private static final List<String> nicknames = Arrays.asList(
		"노래부르는 다람쥐", "부끄러워하는 코끼리", "춤추는 강아지", "고백하는 고양이 ", "거절하는 거북이 ", "손을 번쩍든 오리"
	);

	private final JwtGenerator jwtGenerator;
	private final JwtValidator jwtValidator;
	private final JwtParser jwtParser;
	private final JwtRemover jwtRemover;
	private final SchedulingRemindService schedulingRemindService;
	private final SchedulingAjajaRemindService schedulingAjajaRemindService;

	@Operation(summary = "가짜 로그인 API")
	@PostMapping("/login")
	@ResponseStatus(OK)
	AjajaResponse<AuthResponse.Token> login(@RequestBody AuthRequest.Login request) {
		AuthResponse.Token response = jwtGenerator.login(1L);
		return AjajaResponse.ok(response);
	}

	@Operation(summary = "가짜 Token 재발급 API")
	@PostMapping("/reissue")
	@ResponseStatus(OK)
	AjajaResponse<AuthResponse.Token> reissue(@RequestBody AuthRequest.Reissue request) {
		jwtValidator.validateReissuableAndExtractId(request.getAccessToken(), request.getRefreshToken());
		AuthResponse.Token response = jwtGenerator.reissue(1L, request.getRefreshToken());
		return AjajaResponse.ok(response);
	}

	@Operation(summary = "가짜 로그아웃 API")
	@PostMapping("/users/logout")
	@ResponseStatus(OK)
	void logout() {
		jwtRemover.remove(1L);
	}

	@Operation(summary = "가짜 닉네임 새고로침 API")
	@PostMapping("/users/refresh")
	@ResponseStatus(OK)
	Map<String, String> refreshNickname(@RequestHeader(AUTHORIZATION) String accessToken) {
		String token = extractToken(accessToken);
		jwtParser.parseId(token);

		Collections.shuffle(nicknames);
		Map<String, String> response = new HashMap<>();
		response.put("nickname", nicknames.get(0));
		return response;
	}

	@Operation(summary = "가짜 이메일 인증 요청 API")
	@PostMapping("/users/send-verification")
	@ResponseStatus(OK)
	Map<String, String> sendVerification(@RequestHeader(AUTHORIZATION) String accessToken) {
		String token = extractToken(accessToken);
		jwtParser.parseId(token);

		Map<String, String> response = new HashMap<>();
		response.put("certification", CERTIFICATION);
		return response;
	}

	@Operation(summary = "가짜 이메일 인증 확인 API")
	@PostMapping("/users/verify-email")
	@ResponseStatus(OK)
	void verifyEmail(@RequestBody String certification) {
		if (!CERTIFICATION.equals(certification)) {
			throw new IllegalArgumentException("wrong certification");
		}
	}

	@Operation(summary = "가짜 회원탈퇴 API")
	@DeleteMapping("/users")
	@ResponseStatus(OK)
	void withdraw(@RequestHeader(AUTHORIZATION) String accessToken) {
		String token = extractToken(accessToken);
		jwtParser.parseId(token);
	}

	private String extractToken(String token) {
		return token.substring("Bearer ".length());
	}

	@Operation(summary = "[QA용] 아자자 즉시 전송")
	@PostMapping("/reminds/ajaja")
	@ResponseStatus(OK)
	public AjajaResponse<Void> sendAjajaRemind() {
		schedulingAjajaRemindService.scheduleMorningRemind();

		return AjajaResponse.ok();
	}

	@Operation(summary = "[테스트] 서버 시간 구하기")
	@GetMapping("/time")
	@ResponseStatus(OK)
	public AjajaResponse<Instant> getServerTime() {
		return new AjajaResponse<>(true, Instant.now());
	}

	@Operation(summary = "[테스트] 아침 리마인드 즉시 전송")
	@PostMapping("/reminds/morning")
	@ResponseStatus(OK)
	public AjajaResponse<Void> sendMorningReminds() {
		schedulingRemindService.scheduleMorningRemind();
		return new AjajaResponse<>(true, null);
	}

	@Operation(summary = "[테스트] 점심 리마인드 즉시 전송")
	@PostMapping("/reminds/afternoon")
	@ResponseStatus(OK)
	public AjajaResponse<Void> sendAfternoonReminds() {
		schedulingRemindService.scheduleAfternoonRemind();
		return new AjajaResponse<>(true, null);
	}

	@Operation(summary = "[테스트] 저녁 리마인드 즉시 전송")
	@PostMapping("/reminds/evening")
	@ResponseStatus(OK)
	public AjajaResponse<Void> sendEveningReminds() {
		schedulingRemindService.scheduleEveningRemind();
		return new AjajaResponse<>(true, null);
	}

	@Operation(summary = "[토큰 필요] 비시즌일때 리마인드 조회 API", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 계획에 대한 정보를 불러왔습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "계획 정보를 불러오지 못했습니다.",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 문제입니다. 관리자에게 문의 바랍니다.",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/reminds/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<RemindResponse.RemindInfo> getReminds(@PathVariable Long planId) {
		List<RemindResponse.Message> messages = List.of(
			new RemindResponse.Message(
				"잘하고 있지?",
				9,
				13,
				false
			),
			new RemindResponse.Message(
				"조금만 더 힘내!",
				12,
				13,
				false
			));

		RemindResponse.RemindInfo response = new RemindResponse.RemindInfo(
			"MORNING",
			true,
			12,
			3,
			1,
			messages
		);

		return new AjajaResponse<>(true, response);
	}

	@Operation(summary = "[토큰 필요] 시즌일때 리마인드 조회 API", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 계획에 대한 정보를 불러왔습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "계획 정보를 불러오지 못했습니다.",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 문제입니다. 관리자에게 문의 바랍니다.",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/reminds/modify/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<RemindResponse.RemindInfo> getRemindsInfo(@PathVariable Long planId) {
		List<RemindResponse.Message> responses = List.of(
			new RemindResponse.Message(
				"화이팅",
				6,
				13,
				false
			),
			new RemindResponse.Message(
				"아좌좌",
				12,
				13,
				false
			));

		RemindResponse.RemindInfo response = new RemindResponse.RemindInfo(
			"MORNING",
			true,
			12,
			3,
			1,
			responses
		);

		return new AjajaResponse<>(true, response);
	}

	@Operation(summary = "[토큰 필요] 피드백 반영 API", description = "<b>평가할 계획에 대한 id가 필요합니다.</b>",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공적으로 계획에 대한 정보를 불러왔습니다."),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "평가할 피드백에 대한 정보가 존재하지 않습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 문제입니다. 관리자에게 문의 바랍니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		})
	@PostMapping("/feedbacks/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updateFeedback(
		@PathVariable int planId,
		@RequestBody FeedbackRequest.UpdateFeedback updateFeedback
	) {
		return new AjajaResponse<>(true, null);
	}

	@Operation(summary = "[토큰 필요] 모든 피드백 조회 API", description = "<b>조회할 계획에 대한 id가 필요합니다.</b>",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공적으로 계획에 대한 정보를 불러왔습니다."),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "평가할 피드백에 대한 정보가 존재하지 않습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 문제입니다. 관리자에게 문의 바랍니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		})
	@GetMapping("/feedbacks/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<MockFeedbackResponse.FeedbackInfo> getFeedbacks(
		@PathVariable int planId
	) {
		MockFeedbackResponse.RemindedFeedback feedback1 =
			new MockFeedbackResponse.RemindedFeedback(
				50, "화이팅", 3, 12, true
			);
		MockFeedbackResponse.RemindedFeedback feedback2 =
			new MockFeedbackResponse.RemindedFeedback(0, "", 6, 12, true);
		MockFeedbackResponse.RemindedFeedback feedback3 =
			new MockFeedbackResponse.RemindedFeedback(0, "", 9, 12, false);
		MockFeedbackResponse.RemindedFeedback feedback4 =
			new MockFeedbackResponse.RemindedFeedback(0, "", 12, 12, false);

		List<MockFeedbackResponse.RemindedFeedback> feedbacks = List.of(feedback1, feedback2, feedback3, feedback4);
		MockFeedbackResponse.FeedbackInfo feedbackInfo =
			new MockFeedbackResponse.FeedbackInfo(12, "1일 1커밋", 9, feedbacks);

		return AjajaResponse.ok(feedbackInfo);
	}

	@Operation(summary = "[테스트] 계획 생성 API")
	@PostMapping("/plans")
	@ResponseStatus(CREATED)
	public AjajaResponse<PlanResponse.Create> createPlan(@RequestBody PlanRequest.Create request,
		@RequestHeader(name = "Date") String date) {
		List<String> tags = List.of("tag1", "tag2", "tag3");
		PlanResponse.Create response = new PlanResponse.Create(1L, 1L, "title", "des",
			1, true, true, true, tags);

		return new AjajaResponse<>(true, response);
	}

	@Operation(summary = "[테스트] 계획 전체 조회 API")
	@GetMapping("/plans")
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanResponse.GetAll>> getAllPlans() {
		List<PlanResponse.GetAll> responses = new ArrayList<>();
		List<String> tags = List.of("tag1", "tag2", "tag3");

		for (long i = 0; i < 10; i++) {
			responses.add(
				new PlanResponse.GetAll(i, 1L, "노래하는 다람쥐", "제목",
					1, 10, tags, Instant.parse("2023-01-02T08:19:23Z"))
			);
		}

		return new AjajaResponse<>(true, responses);
	}

	@Operation(summary = "[테스트] 계획 단건 조회 API")
	@GetMapping("/plans/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.GetOne> getOnePlan(@PathVariable Long id) {
		List<String> tags = List.of("술", "금주", "알코올 중독");
		PlanResponse.GetOne response = new PlanResponse.GetOne(1L, 1L, "노래하는 다람쥐", "술 줄이기",
			"술 한 달에 두번만 먹기", 1, true, true, true, 15, true, tags,
			Instant.parse("2023-01-04T04:14:14Z"));

		return new AjajaResponse<>(true, response);
	}

	@Operation(summary = "[테스트] 계획 수정 API")
	@PutMapping("/plans/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.Create> updatePlan(@PathVariable Long id,
		@RequestBody PlanRequest.Update request, @RequestHeader(name = "Date") String date) {
		List<String> tags = List.of("tag1", "tag2", "tag3");
		PlanResponse.Create updated = new PlanResponse.Create(1L, 1L, "title", "des",
			1, true, true, true, tags);

		return new AjajaResponse<>(true, updated);
	}

	@Operation(summary = "[테스트] 계획 삭제 API")
	@DeleteMapping("/plans/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<?> deletePlan(@PathVariable Long id, @RequestHeader(name = "Date") String date) {
		return AjajaResponse.ok();
	}

	@Operation(summary = "[테스트] 계획 공개 여부 변경 API")
	@PutMapping("/plans/{id}/public")
	@ResponseStatus(OK)
	public AjajaResponse<?> updatePlanPublicStatus(@PathVariable Long id) {
		return AjajaResponse.ok();
	}

	@Operation(summary = "[테스트] 계획 리마인드 알림 여부 변경 API")
	@PutMapping("/plans/{id}/remindable")
	@ResponseStatus(OK)
	public AjajaResponse<?> updatePlanRemindStatus(@PathVariable Long id) {
		return AjajaResponse.ok();
	}

	@Operation(summary = "[테스트] 응원메시지 알림 여부 변경 API")
	@PutMapping("/plans/{id}/ajaja")
	@ResponseStatus(OK)
	public AjajaResponse<?> updatePlanAjajaStatus(@PathVariable Long id) {
		return AjajaResponse.ok();
	}

	@Operation(summary = "[토큰 필요] 메인 페이지 내 계획 조회 API", description = "로그인을 했을 시에만 불러올 수 있습니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공적으로 계획에 대한 정보를 불러왔습니다."),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 문제입니다. 관리자에게 문의 바랍니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		})
	@GetMapping("/plans/main")
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanResponse.MainInfo>> getPlanInfo() {
		List<PlanResponse.PlanInfo> getPlan2023 = List.of(
			new PlanResponse.PlanInfo(2023, 1L, "매일 운동하기", true, 90, 1),
			new PlanResponse.PlanInfo(2023, 2L, "매일 코딩하기", true, 90, 2),
			new PlanResponse.PlanInfo(2023, 3L, "매일 아침 9시에 일어나기", false, 20, 3)
		);

		List<PlanResponse.PlanInfo> getPlan2022 = List.of(
			new PlanResponse.PlanInfo(2022, 4L, "졸업 작품 끝내기", true, 90, 1),
			new PlanResponse.PlanInfo(2022, 5L, "매일 아침 먹기", true, 70, 2),
			new PlanResponse.PlanInfo(2022, 6L, "총 학점 4.0 이상 나오기", false, 50, 3)
		);

		PlanResponse.MainInfo getPlanInfo2023 = new PlanResponse.MainInfo(2023,
			50, getPlan2023);

		PlanResponse.MainInfo getPlanInfo2022 = new PlanResponse.MainInfo(2022,
			80,
			getPlan2022);

		List<PlanResponse.MainInfo> getPlanInfo = List.of(getPlanInfo2023, getPlanInfo2022);
		return AjajaResponse.ok(getPlanInfo);
	}
}
