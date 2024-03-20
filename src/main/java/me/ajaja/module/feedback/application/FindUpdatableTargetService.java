package me.ajaja.module.feedback.application;

import java.util.List;

import me.ajaja.module.feedback.application.model.UpdatableFeedback;

public interface FindUpdatableTargetService { // todo: 헥사고날 아키텍쳐 적용 후 네이밍 변경
	List<UpdatableFeedback> findUpdatableTargetsByUserId(Long userId);
}
