package com.newbarams.ajaja.module.user.presentation;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.user.application.ChangeReceiveTypeService;
import com.newbarams.ajaja.module.user.application.LogoutService;
import com.newbarams.ajaja.module.user.application.RenewNicknameService;
import com.newbarams.ajaja.module.user.application.SendVerificationEmailService;
import com.newbarams.ajaja.module.user.application.VerifyCertificationService;
import com.newbarams.ajaja.module.user.application.WithdrawService;
import com.newbarams.ajaja.module.user.domain.repository.UserQueryRepository;
import com.newbarams.ajaja.module.user.dto.UserRequest;
import com.newbarams.ajaja.module.user.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "user", description = "사용자 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final LogoutService logoutService;
	private final WithdrawService withdrawService;
	private final UserQueryRepository userQueryRepository;
	private final RenewNicknameService renewNicknameService;
	private final ChangeReceiveTypeService changeReceiveTypeService;
	private final VerifyCertificationService verifyCertificationService;
	private final SendVerificationEmailService sendVerificationEmailService;

	@Operation(summary = "[토큰 필요] 마이페이지 API", description = "사용자의 정보를 불러옵니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 정보를 불러왔습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다.")
	})
	@GetMapping
	@ResponseStatus(OK)
	public AjajaResponse<UserResponse.MyPage> getMyPage(@UserId Long id) {
		UserResponse.MyPage response = userQueryRepository.findUserInfoById(id);
		return AjajaResponse.ok(response);
	}

	@Operation(summary = "[토큰 필요] 닉네임 새로고침 API", description = "새로운 랜덤 닉네임을 생성합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 새로운 닉네임으로 변경했습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다.")
	})
	@PostMapping("/refresh")
	@ResponseStatus(OK)
	public AjajaResponse<String> renewNickname(@UserId Long id) {
		String newNickname = renewNicknameService.renew(id);
		return AjajaResponse.ok(newNickname);
	}

	@Operation(summary = "[토큰 필요] 이메일 검증 요청 API", description = "리마인드 받을 이메일을 검증하기 위해 인증을 요청합니다.", responses = {
		@ApiResponse(responseCode = "204", description = "성공적으로 인증 메일을 전송했습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
		@ApiResponse(responseCode = "409", description = "이메일 인증을 할 수 없습니다. 기존 리마인드 이메일과 다른 이메일을 입력해야 합니다.")
	})
	@PostMapping("/send-verification")
	@ResponseStatus(NO_CONTENT)
	public void sendVerification(
		@UserId Long id,
		@Valid @RequestBody UserRequest.EmailVerification request
	) {
		sendVerificationEmailService.sendVerification(id, request.email());
	}

	@Operation(summary = "[토큰 필요] 인증 번호 검증 API", description = "발송된 인증 번호를 검증합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 이메일 인증을 완료하였습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
		@ApiResponse(responseCode = "409", description = "인증 번호가 일치하지 않습니다."),
	})
	@PostMapping("/verify")
	@ResponseStatus(OK)
	public AjajaResponse<Void> verifyCertification(
		@UserId Long id,
		@Valid @RequestBody UserRequest.Certification request
	) {
		verifyCertificationService.verify(id, request.certification());
		return AjajaResponse.ok();
	}

	@Operation(summary = "[토큰 필요] 로그아웃 API", description = "발급된 사용자의 토큰을 만료시킵니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 로그아웃하였습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
	})
	@PostMapping("/logout")
	@ResponseStatus(NO_CONTENT)
	public void logout(@UserId Long id) {
		logoutService.logout(id);
	}

	@Operation(summary = "[토큰 필요] 수신 종류 변경 API", description = "리마인드를 수신 방법을 변경합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 수신 방법이 변경되었습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
	})
	@PutMapping("/receive")
	@ResponseStatus(OK)
	public AjajaResponse<Void> changeReceiveType(
		@UserId Long id,
		@Schema(allowableValues = {"kakao", "email", "both"}) @RequestParam String type
	) {
		changeReceiveTypeService.change(id, type);
		return AjajaResponse.ok();
	}

	@Operation(summary = "[토큰 필요] 회원 탈퇴 API", description = "인가 코드를 통해서 회원 탈퇴를 진행합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 회원 탈퇴를 하였습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
	})
	@DeleteMapping
	@ResponseStatus(OK)
	public AjajaResponse<Void> withdraw(@UserId Long id) {
		withdrawService.withdraw(id);
		return AjajaResponse.ok();
	}
}
