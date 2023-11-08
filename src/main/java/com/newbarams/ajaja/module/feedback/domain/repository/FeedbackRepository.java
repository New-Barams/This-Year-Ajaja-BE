package com.newbarams.ajaja.module.feedback.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.newbarams.ajaja.module.feedback.domain.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>, FeedbackRepositoryCustom {
	@Query(value = """
		SELECT NEW com.newbarams.ajaja.module.feedback.domain.Feedback(
			f.userId, f.planId , f.achieve
		)
		FROM feedbacks f
		WHERE year(f.createdAt) == year(now()) AND f.userId == :planId
		""", nativeQuery = true)
	List<Feedback> findAllByPlanIdIdAndCreatedYear(Long planId);
}
