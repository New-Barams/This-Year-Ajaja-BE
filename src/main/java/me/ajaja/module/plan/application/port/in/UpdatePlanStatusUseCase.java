package me.ajaja.module.plan.application.port.in;

public interface UpdatePlanStatusUseCase {
	void updatePublicStatus(Long id, Long userId);

	void updateRemindStatus(Long id, Long userId);

	void updateAjajaStatus(Long id, Long userId);
}
