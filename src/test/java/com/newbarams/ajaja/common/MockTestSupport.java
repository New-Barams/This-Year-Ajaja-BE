package com.newbarams.ajaja.common;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Supporting Mock test with Fixture-Monkey.
 * Monkey is based on ConstructorProperties and NEVER return null.
 */
@ExtendWith(MockitoExtension.class)
public abstract class MockTestSupport extends MonkeySupport {
}
