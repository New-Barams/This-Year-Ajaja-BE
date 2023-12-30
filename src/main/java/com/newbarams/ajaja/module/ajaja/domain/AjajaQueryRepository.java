package com.newbarams.ajaja.module.ajaja.domain;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.remind.application.model.RemindableAjaja;

@Repository
public interface AjajaQueryRepository {
	List<RemindableAjaja> findRemindableAjaja();
}
