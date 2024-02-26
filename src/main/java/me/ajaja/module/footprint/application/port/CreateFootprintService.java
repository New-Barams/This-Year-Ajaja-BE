package me.ajaja.module.footprint.application.port;

import static me.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.footprint.application.port.in.CreateFootprintUseCase;
import me.ajaja.module.footprint.application.port.out.CreateFootprintPort;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.FootprintFactory;
import me.ajaja.module.footprint.domain.Target;
import me.ajaja.module.footprint.domain.Writer;
import me.ajaja.module.footprint.dto.FootprintParam;
import me.ajaja.module.plan.application.port.out.FindPlanPort;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.user.application.port.out.RetrieveUserPort;
import me.ajaja.module.user.domain.User;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateFootprintService implements CreateFootprintUseCase {
	private final FootprintFactory footprintFactory;
	private final CreateFootprintPort createFootprintPort;
	private final RetrieveUserPort retrieveUserPort;
	private final FindPlanPort findPlanPort;

	@Override
	public Long create(Long userId, Long targetId, FootprintParam.Create param) {
		User user = retrieveUserPort.loadById(userId).orElseThrow(() -> new AjajaException(USER_NOT_FOUND));
		Writer writer = new Writer(user.getId(), user.getNickname().getNickname());

		Plan plan = findPlanPort.findById(targetId).orElseThrow(() -> new AjajaException(NOT_FOUND_PLAN));
		Target target = new Target(plan.getId(), plan.getPlanTitle());

		Footprint footprint = footprintFactory.create(target, writer, param);

		return createFootprintPort.create(footprint);
	}
}
