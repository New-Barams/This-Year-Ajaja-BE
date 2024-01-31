package com.newbarams.ajaja.module.plan.application;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.newbarams.ajaja.module.plan.domain.BanWordFilter;
import com.newbarams.ajaja.module.plan.dto.BanWordValidationResult;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ValidateContentService {
	private static final int MAP_CAPACITY = 2;
	private static final String CONTENT_TITLE = "title";
	private static final String CONTENT_DESCRIPTION = "description";

	private final BanWordFilter banWordFilter;

	public Map<String, BanWordValidationResult> check(PlanRequest.CheckBanWord request) {
		Map<String, BanWordValidationResult> result = new HashMap<>(MAP_CAPACITY);

		result.put(CONTENT_TITLE, banWordFilter.validate(request.getTitle()));
		result.put(CONTENT_DESCRIPTION, banWordFilter.validate(request.getDescription()));

		return result;
	}
}
