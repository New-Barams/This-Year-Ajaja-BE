package com.newbarams.ajaja.module.user.application.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
@MockBean({
	ChangeReceiveTypeService.class,
	LogoutService.class,
	ReissueTokenService.class,
	RenewNicknameService.class,
	SendVerificationEmailService.class,
	VerifyCertificationService.class,
	WithdrawService.class
})
public class UserMockBeans {
}
