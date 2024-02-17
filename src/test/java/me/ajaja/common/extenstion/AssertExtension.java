package me.ajaja.common.extenstion;

import static org.assertj.core.api.ThrowableAssert.*;

import org.assertj.core.api.ThrowableAssertAlternative;
import org.assertj.core.api.ThrowableTypeAssert;

import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;

public class AssertExtension {

	public static CustomThrowableTypeAssert assertThatAjajaException(ErrorCode errorCode) {
		return new CustomThrowableTypeAssert(AjajaException.class, errorCode);
	}

	public static class CustomThrowableTypeAssert extends ThrowableTypeAssert<AjajaException> {
		private final ErrorCode errorCode;

		public CustomThrowableTypeAssert(Class<? extends AjajaException> throwableType, ErrorCode errorCode) {
			super(throwableType);
			this.errorCode = errorCode;
		}

		@Override
		public ThrowableAssertAlternative<AjajaException> isThrownBy(ThrowingCallable throwingCallable) {
			return super.isThrownBy(throwingCallable)
				.withMessage(errorCode.getMessage());
		}
	}
}
