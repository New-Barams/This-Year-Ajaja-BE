package com.newbarams.ajaja.module.auth.application;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
@MockBean({
	LoginService.class,
})
public class AuthMockBeans {
}
