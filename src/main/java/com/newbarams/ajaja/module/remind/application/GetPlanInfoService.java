package com.newbarams.ajaja.module.remind.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.remind.application.port.in.GetPlanInfoUseCase;
import com.newbarams.ajaja.module.remind.application.port.out.FindPlanInfoPort;
import com.newbarams.ajaja.module.remind.application.port.out.LoadTotalAchievePort;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetPlanInfoService implements GetPlanInfoUseCase {
	private final LoadTotalAchievePort loadTotalAchievePort;
	private final FindPlanInfoPort findPlanInfoPort;
	private final PlanMapper mapper;

	public List<PlanResponse.MainInfo> load(Long userId) {
		List<PlanResponse.PlanInfo> planInfos = findPlanInfoPort.findAllPlanInfosByUserId(userId);

		if (planInfos.isEmpty()) {
			return Collections.emptyList();
		}

		int currentYear = planInfos.get(0).getYear();
		int firstYear = planInfos.get(planInfos.size() - 1).getYear();

		return loadPlanInfoResponses(currentYear, firstYear, planInfos, userId);
	}

	private List<PlanResponse.MainInfo> loadPlanInfoResponses(
		int currentYear,
		int firstYear,
		List<PlanResponse.PlanInfo> planInfos,
		Long userId
	) {
		List<PlanResponse.MainInfo> planInfoResponses = new ArrayList<>();

		for (int planYear = currentYear; planYear >= firstYear; planYear--) {
			int totalAchieve = (int)planInfos.stream().filter(plan -> plan.getYear() == currentYear).mapToDouble(
				PlanResponse.PlanInfo::getAchieveRate).average().orElse(0);
			PlanResponse.MainInfo planInfoResponse = mapper.toResponse(planYear, totalAchieve, planInfos);
			planInfoResponses.add(planInfoResponse);
		}

		return planInfoResponses;
	}
}
