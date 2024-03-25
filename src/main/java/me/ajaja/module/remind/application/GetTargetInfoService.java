package me.ajaja.module.remind.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.remind.application.port.in.GetTargetInfoUseCase;
import me.ajaja.module.remind.application.port.out.FindTargetInfoPort;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class GetTargetInfoService implements GetTargetInfoUseCase {
	private final FindTargetInfoPort findTargetInfoPort;
	private final PlanMapper mapper;

	public List<PlanResponse.MainInfo> load(Long userId) {
		List<PlanResponse.PlanInfo> planInfos = findTargetInfoPort.findAllPlanInfosByUserId(userId);

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
