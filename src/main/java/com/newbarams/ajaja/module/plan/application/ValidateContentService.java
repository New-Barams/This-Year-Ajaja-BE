package com.newbarams.ajaja.module.plan.application;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.newbarams.ajaja.module.plan.domain.BanWordFilter;
import com.newbarams.ajaja.module.plan.dto.BanWordValidationResult;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateContentService {
	public BanWordValidationResult check(PlanRequest.CheckBanWord request) {
		BanWordValidationResult.Common titleResult = getResult(request.getTitle());
		BanWordValidationResult.Common descriptionResult = getResult(request.getDescription());

		return new BanWordValidationResult(titleResult, descriptionResult);
	}

	private BanWordValidationResult.Common getResult(String origin) {
		List<String> result = BanWordFilter.validate(origin);

		if (result.isEmpty()) {
			return new BanWordValidationResult.Common(false, Collections.emptyList());
		}

		return new BanWordValidationResult.Common(true, result);
	}
}
