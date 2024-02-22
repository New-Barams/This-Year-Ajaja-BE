package me.ajaja.module.remind.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RemindDate {
	private final int month;
	private final int day;
}
