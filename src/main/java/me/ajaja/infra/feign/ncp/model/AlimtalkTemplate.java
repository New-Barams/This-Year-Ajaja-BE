package me.ajaja.infra.feign.ncp.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
enum AlimtalkTemplate {
	REMIND("""
		[올해도 아좌좌🔥]
		   
		계획 잘 지키고 계신가요?!
		요청하신 리마인드를 확인하세요 👻
		   
		▶ 내 계획 :
		%s
		   
		▶ 리마인드 메시지 :
		%s
		   
		계획을 잘 지키고 있었는지
		%d월 %d일까지 피드백해보세요!
		"""),
	AJAJA("""
		[올해도 아좌좌🔥]
				
		요청하신 응원 리마인드입니다!
		지난 주에 내 계획을 %d명이나 응원했어요 👏👏
				
		▶ 내 계획
		%s
				
		이 기세를 몰아서 아좌좌 💪
		""");

	private final String content;

	public String getTemplateCode() {
		return this.name().toLowerCase();
	}

	public String content(String planName, String message, int month, int day) {
		if (this.equals(AJAJA)) {
			throw new UnsupportedOperationException("This method is for REMIND");
		}

		return content.formatted(planName, message, month, day);
	}

	public String content(Long ajajaCount, String planName) {
		if (this.equals(REMIND)) {
			throw new UnsupportedOperationException("This method is for AJAJA");
		}

		return content.formatted(ajajaCount, planName);
	}
}
