package me.ajaja.module.user.domain;

import java.util.Random;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class RandomNicknameGenerator {
	private static final Random random = new Random();
	private static final String UNDER_BAR = "_";
	private static final String BLANK = " ";

	public static String generate() {
		return randomBehavior() + BLANK + randomAnimal();
	}

	private static String randomBehavior() {
		return randomValue(Behavior.values()).replace(UNDER_BAR, BLANK);
	}

	private static String randomAnimal() {
		return randomValue(Animal.values());
	}

	private static String randomValue(Enum[] values) {
		int randomIndex = random.nextInt(values.length);
		return values[randomIndex].name();
	}

	private enum Behavior {
		노래하는, 복싱하는, 고백하는, 운동하는, 청소하는,
		춤추는, 달리는, 책_읽는, 음악_듣는, 요리하는,
		공부하는, 꿀잠_자는, 라면_먹는, 수영하는, 게임하는,
		목욕하는, 연주하는, 기타_치는, 빵_굽는, 서핑하는,
	}

	private enum Animal {
		코끼리, 거북이, 고양이, 강아지, 다람쥐,
		돌고래, 햄스터, 원숭이, 호랑이, 캥거루,
		사자, 참새, 돼지, 토끼, 고래,
		생쥐, 수달, 펭귄, 사슴, 오리,
		고슴도치, 사막여우,
		소, 양, 곰,
	}
}
