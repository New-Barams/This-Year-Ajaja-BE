package com.newbarams.ajaja.module.feedback.infra;

import org.springframework.data.jpa.repository.JpaRepository;

interface FeedbackJpaRepository extends JpaRepository<FeedbackEntity, Long> {
}
