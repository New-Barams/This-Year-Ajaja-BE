package me.ajaja.module.plan.application.port.in;

public interface DeletePlanUseCase {
	void delete(Long id, Long userId, int month);
}
