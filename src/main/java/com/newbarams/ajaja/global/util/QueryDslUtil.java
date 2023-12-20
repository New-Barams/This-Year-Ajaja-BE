package com.newbarams.ajaja.global.util;

import static com.newbarams.ajaja.module.plan.infra.QPlanEntity.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;

public class QueryDslUtil {
	private static final List<Integer> MONTHS_PER_ONE_MONTH = new ArrayList<>(List.of(2, 4, 5, 7, 8, 10, 11));
	private static final List<Integer> MONTHS_PER_THREE_MONTH = new ArrayList<>(List.of(3, 9));
	private static final List<Integer> MONTHS_PER_SIX_MONTH = new ArrayList<>(List.of(6));

	private QueryDslUtil() {
	}

	public static BooleanExpression isRemindMonth() {
		Instant instant = Instant.now();
		ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

		int month = zonedDateTime.getMonthValue();

		if (MONTHS_PER_ONE_MONTH.contains(month)) {
			return planEntity.remindTerm.eq(1);
		} else if (MONTHS_PER_THREE_MONTH.contains(month)) {
			return planEntity.remindTerm.in(1, 3);
		} else if (MONTHS_PER_SIX_MONTH.contains(month)) {
			return planEntity.remindTerm.in(1, 3, 6);
		}

		return planEntity.remindTerm.in(1, 3, 6, 12);
	}
}
