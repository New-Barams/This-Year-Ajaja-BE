package com.newbarams.ajaja.module.ajaja.domain.repository;

import java.util.List;

import com.newbarams.ajaja.module.remind.application.model.RemindableAjaja;

public interface AjajaQueryRepository {
	List<RemindableAjaja> findRemindableAjaja();
}
