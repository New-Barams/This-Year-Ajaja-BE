package com.newbarams.ajaja.module.plan.dto;

import java.util.Collections;
import java.util.List;

import org.ahocorasick.trie.Emit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BanWordValidationResult {
	private final boolean existBanWord;
	private final String originSentence;
	private final List<Emit> banWordResults;

	public BanWordValidationResult(boolean existBanWord, String originSentence) {
		this.existBanWord = existBanWord;
		this.originSentence = originSentence;
		this.banWordResults = Collections.emptyList();
	}
}
