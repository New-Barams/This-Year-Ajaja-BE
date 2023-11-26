package com.newbarams.ajaja.module.user.application;

import org.springframework.stereotype.Service;

@Service
public interface DisablePlanService {
	void disable(Long userId);
}
