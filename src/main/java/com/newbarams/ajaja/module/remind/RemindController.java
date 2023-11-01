package com.newbarams.ajaja.module.remind;

import static org.springframework.http.HttpStatus.*;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.module.remind.domain.dto.CommonResponse;
import com.newbarams.ajaja.module.remind.domain.dto.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "remind", description = "리마인드 API")
@RestController
@RequestMapping("/reminds")
public class RemindController {

	@Tag(name = "remind")
	@Operation(description = "[테스트] 리마인드 정보 조회 API", responses = {
		@ApiResponse(responseCode = "200", description = "리마인드 정보들을 조회")
	})
	@GetMapping("/list")
	@ResponseStatus(OK)
	public AjajaResponse<CommonResponse> getReminds() {
		List<Response> responses = List.of(
			new Response("화이팅", true, 60, Timestamp.valueOf("2023-4-02 23:59:59")),
			new Response("아좌좌", true, 60, Timestamp.valueOf("2023-7-02 23:59:59")),
			new Response("할수있다", false, 0, Timestamp.valueOf("2023-10-02 23:59:59")));

		CommonResponse commonResponse = new CommonResponse("moring", 2, 3, 1, true, responses);
		return AjajaResponse.ok(commonResponse);
	}
}
