package com.newbarams.ajaja.global.mock;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import com.newbarams.ajaja.module.feedback.domain.dto.GetAchieve;
import com.newbarams.ajaja.module.feedback.domain.dto.UpdateFeedback;
import com.newbarams.ajaja.module.plan.domain.dto.PlanResponse;
import com.newbarams.ajaja.module.remind.domain.dto.GetReminds;
import com.newbarams.ajaja.module.remind.domain.dto.ModifyAlarm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "mock", description = "MOCK API")
@RestController
@RequestMapping("/mock")
class MockController {
	private static final String CERTIFICATION = "123456";
	private static final String ACCESS_TOKEN = "thisismockkakaoaccesstoken";
	private static final String REFRESH_TOKEN = "thisismockkakaorefreshtoken";
	private static final String NEW_ACCESS_TOKEN = "thisisnewmockkakaoaccesstoken";
	private static final List<String> nicknames = Arrays.asList("노래부르는 다람쥐", "부끄러워하는 코끼리", "춤추는 강아지", "고백하는 고양이 ",
		"거절하는 거북이 ", "손을 번쩍든 오리");

	@Tag(name = "mock")
	@Operation(description = "가짜 회원가입 API")
	@PostMapping("/signup")
	@ResponseStatus(OK)
	void signup() {
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 로그인 API")
	@PostMapping("/login")
	@ResponseStatus(OK)
	Map<String, String> login() {
		Map<String, String> response = new HashMap<>();
		response.put("accessToken", ACCESS_TOKEN);
		response.put("refreshToken", REFRESH_TOKEN);
		return response;
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 Token 재발급 API")
	@PostMapping("/reissue")
	@ResponseStatus(OK)
	Map<String, String> reissue(@RequestHeader(AUTHORIZATION) String refreshToken) {
		String token = extractToken(refreshToken);
		validateRefreshToken(token);

		Map<String, String> response = new HashMap<>();
		response.put("accessToken", NEW_ACCESS_TOKEN);
		return response;
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 로그아웃 API")
	@PostMapping("/logout")
	@ResponseStatus(OK)
	void logout() {
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 닉네임 새고로침 API")
	@PostMapping("/users/refresh")
	@ResponseStatus(OK)
	Map<String, String> refreshNickname(@RequestHeader(AUTHORIZATION) String accessToken) {
		String token = extractToken(accessToken);
		validateAccessToken(token);

		Collections.shuffle(nicknames);
		Map<String, String> response = new HashMap<>();
		response.put("nickname", nicknames.get(0));
		return response;
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 이메일 인증 요청 API")
	@PostMapping("/users/send-verification")
	@ResponseStatus(OK)
	Map<String, String> sendVerification(@RequestHeader(AUTHORIZATION) String accessToken) {
		String token = extractToken(accessToken);
		validateAccessToken(token);

		Map<String, String> response = new HashMap<>();
		response.put("certification", CERTIFICATION);
		return response;
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 이메일 인증 확인 API")
	@PostMapping("/users/verify-email")
	@ResponseStatus(OK)
	void verifyEmail(@RequestBody String certification) {
		if (!CERTIFICATION.equals(certification)) {
			throw new IllegalArgumentException("wrong certification");
		}
	}

	@Tag(name = "mock")
	@Operation(description = "가짜 회원탈퇴 API")
	@DeleteMapping("/users")
	@ResponseStatus(OK)
	void withdraw(@RequestHeader(AUTHORIZATION) String accessToken) {
		String token = extractToken(accessToken);
		validateAccessToken(token);
	}

	private String extractToken(String token) {
		return token.substring("Bearer ".length());
	}

	private void validateAccessToken(String accessToken) {
		if (!ACCESS_TOKEN.equals(accessToken)) {
			throw new IllegalArgumentException("wrong access token");
		}
	}

	private void validateRefreshToken(String refreshToken) {
		if (!ACCESS_TOKEN.equals(refreshToken)) {
			throw new IllegalArgumentException("wrong refresh token");
		}
	}

	@Tag(name = "mock")
	@Operation(description = "[테스트-5초 후 발송] 리마인드 전송 API")
	@PostMapping("/plans/{planId}/reminds")
	@ResponseStatus(OK)
	public AjajaResponse<GetReminds.Response> sendRemind() throws InterruptedException {
		TimeUnit.SECONDS.sleep(5);
		GetReminds.Response response = new GetReminds.Response(1, "화이팅", false, 0,
			new Timestamp(System.currentTimeMillis()));

		return new AjajaResponse<>(true, response);
	}

	@Tag(name = "mock")
	@Operation(description = "[테스트] 리마인드 정보 조회 API")
	@GetMapping("/plans/{planId}/reminds")
	@ResponseStatus(OK)
	public AjajaResponse<GetReminds.CommonResponse> getReminds() {
		List<GetReminds.Response> responses = List.of(
			new GetReminds.Response(1, "화이팅", true, 60, Timestamp.valueOf("2023-4-02 23:59:59")),
			new GetReminds.Response(2, "아좌좌", true, 60, Timestamp.valueOf("2023-7-02 23:59:59")),
			new GetReminds.Response(3, "할수있다", false, 0, Timestamp.valueOf("2023-10-02 23:59:59")));

		GetReminds.CommonResponse commonResponse = new GetReminds.CommonResponse("moring", 2, 3, 1, true, responses);
		return new AjajaResponse<>(true, commonResponse);
	}

	@Tag(name = "mock")
	@Operation(description = "[테스트] 리마인드 알림 끄기 API")
	@PutMapping("/plans/{planId}/reminds")
	@ResponseStatus(OK)
	public AjajaResponse<String> modifyRemindAlarm(
		@PathVariable int planId,
		@RequestBody ModifyAlarm modifyAlarm
	) {
		return new AjajaResponse<>(true, null);
	}

	@Tag(name = "mock")
	@Operation(description = "[테스트] 피드백 수행 API")
	@PostMapping("/feedbacks/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updateFeedback(
		@PathVariable int planId,
		@RequestBody UpdateFeedback updateFeedback
	) {
		return new AjajaResponse<>(true, null);
	}

	@Tag(name = "mock")
	@Operation(description = "[테스트] 목표별 계획률 보기 API")
	@GetMapping("/feedbacks/{planId}")
	@ResponseStatus(OK)
	public AjajaResponse<GetAchieve> findPlanAchieve(
		@PathVariable int planId
	) {
		GetAchieve achieve = new GetAchieve(90);

		return new AjajaResponse<>(true, achieve);
	}

	@Tag(name = "mock")
	@Operation(description = "[테스트] 전체 달성률 보기")
	@GetMapping("/feedbacks/user/{userId}")
	public AjajaResponse<GetAchieve> findTotalAchieve(
		@PathVariable int userId
	) {
		GetAchieve achieve = new GetAchieve(90);

		return new AjajaResponse<>(true, achieve);
	}

	@Tag(name = "mock")
	@Operation(description = "[테스트] 계획 생성 API")
	@PostMapping("/plans")
	@ResponseStatus(CREATED)
	public void createPlan() {

	}

	@Tag(name = "mock")
	@Operation(description = "[테스트] 계획 전체 조회 API")
	@GetMapping("/plans")
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanResponse.GetAll>> getAllPlans() {
		List<PlanResponse.GetAll> responses = new ArrayList<>();
		List<String> tags = List.of("tag1", "tag2", "tag3");

		for (long i = 0; i < 10; i++) {
			responses.add(
				new PlanResponse.GetAll(i, 1L, "노래하는 다람쥐", "제목",
					10, tags, Instant.parse("2023-01-02T08:19:23Z"))
			);
		}

		return new AjajaResponse<>(true, responses);
	}

	@Tag(name = "mock")
	@Operation(description = "[테스트] 계획 단건 조회 API")
	@GetMapping("/plans/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.GetOne> getOnePlan(@PathVariable Long id) {
		List<String> tags = List.of("술", "금주", "알코올 중독");
		PlanResponse.GetOne response = new PlanResponse.GetOne(1L, 1L, "노래하는 다람쥐", "술 줄이기",
			"술 한 달에 두번만 먹기", true, 15, tags, Instant.parse("2023-01-04T04:14:14Z"));

		return new AjajaResponse<>(true, response);
	}

	@Tag(name = "mock")
	@Operation(description = "[테스트] 계획 수정 API")
	@PutMapping("/plans/{id}")
	@ResponseStatus(OK)
	public void updatePlan(@PathVariable Long id) {

	}

	@Tag(name = "mock")
	@Operation(description = "[테스트] 계획 삭제 API")
	@DeleteMapping("/plans/{id}")
	@ResponseStatus(OK)
	public void deletePlan(@PathVariable Long id) {

	}

	@Tag(name = "mock")
	@Operation(description = "[테스트] 계획을 비공개로 수정")
	@PutMapping("/plans/{id}/private")
	@ResponseStatus(OK)
	public void switchPlanToPrivate(@PathVariable Long id) {

	}

	@Tag(name = "mock")
	@Operation(description = "[테스트] 계획을 공개로 수정")
	@PutMapping("/plans/{id}/public")
	@ResponseStatus(OK)
	public void switchPlanToPublic(@PathVariable Long id) {

	}
}
