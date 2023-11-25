package com.newbarams.ajaja.module.feedback.domain.repository;

import java.util.List;

import com.newbarams.ajaja.module.feedback.domain.Feedback;

public interface FeedbackRepositoryCustom {
	List<Feedback> findAllByPlanIdIdAndCreatedYear(Long planId);
}
