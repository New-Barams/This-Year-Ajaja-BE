package com.newbarams.ajaja.module.user.application.service;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MockBean({
	ChangeReceiveTypeService.class,
	LoginService.class,
	LogoutService.class,
	ReissueTokenService.class,
	RenewNicknameService.class,
	SendVerificationEmailService.class,
	VerifyCertificationService.class,
	WithdrawService.class
})
public class UserMockBeans {
}
