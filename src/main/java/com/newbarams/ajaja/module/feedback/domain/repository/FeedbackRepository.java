package com.newbarams.ajaja.module.feedback.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newbarams.ajaja.module.feedback.domain.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
