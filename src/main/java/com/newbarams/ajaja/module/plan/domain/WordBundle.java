package com.newbarams.ajaja.module.plan.domain;

import org.ahocorasick.trie.Trie;

public class WordBundle {
	static final Trie banWord = Trie.builder()
		.ignoreOverlaps()
		.addKeywords("ㅅㅂ", "시발", "지랄", "개새끼", "ㅈㄹ", "씨발", "씨바", "시바", "존나", "ㅈㄴ", "좆같", "ㅈ같", "ㅈㄹ", "미친놈",
			"미친년", "미친새끼", "시팔", "뻐큐", "빙신", "ㅂㅅ", "병신", "쓰렉", "샹년", "썅년", "병딱", "샹놈", "썅놈", "시벌", "디질",
			"뒤질", "디졌", "느금마", "ㄴㄱㅁ", "느금", "맘충", "시빡", "애미", "에미", "애비", "엿같", "옘병", "염병", "씨파", "씨팔",
			"엠창", "씨댕", "씹팔", "씹창", "ㅁㅊ", "씹창", "등신", "늬미", "앰창", "좃까", "좆까", "sibal", "fuck", "shit", "좇까",
			"좆만", "ㅅ발", "ㅆㄹㄱ", "ㄱㄹ", "걸레", "호로", "븅신", "빠큐", "씨이발", "씨바알", "뒤진다", "니년", "슈발", "아닥", "썅",
			"짱깨", "시부랄", "씨부랄", "머갈", "엿같", "ㅈ1랄", "ㅈ1ㄹ", "ㅅ1ㅂ", "ㅁ1ㅊ", "엠생", "앰생")
		.build();

	static final Trie allowed = Trie.builder()
		.addKeyword("")  // 허용 단어 추가 예정
		.build();
}
