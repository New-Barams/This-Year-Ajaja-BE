package me.ajaja.module.ajaja.domain;

import java.util.List;

import org.springframework.stereotype.Repository;

import me.ajaja.module.remind.application.model.RemindableAjaja;

@Repository
public interface AjajaQueryRepository {
	List<RemindableAjaja> findRemindableAjajasByEndPoint(String remindType);
}
