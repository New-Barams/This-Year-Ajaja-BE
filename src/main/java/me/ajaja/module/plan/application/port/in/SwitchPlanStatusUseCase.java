package me.ajaja.module.plan.application.port.in;

public interface SwitchPlanStatusUseCase {
	void switchPublic(Long id, Long userId);

	void switchRemindable(Long id, Long userId);

	void switchAjajable(Long id, Long userId);
}
