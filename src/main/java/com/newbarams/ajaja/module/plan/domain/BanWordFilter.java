package com.newbarams.ajaja.module.plan.domain;

import java.util.Collection;
import java.util.List;

import org.ahocorasick.trie.Emit;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.module.plan.dto.BanWordValidationResult;

@Component
public class BanWordFilter {
	public BanWordValidationResult validate(String originSentence) {
		Collection<Emit> badWordResult = WordBundle.banWord.parseText(originSentence);
		return getResult(badWordResult, originSentence);
	}

	private BanWordValidationResult getResult(Collection<Emit> result, String originSentence) {
		if (result.isEmpty()) {
			return new BanWordValidationResult(false, originSentence);
		}

		return new BanWordValidationResult(true, originSentence, List.copyOf(result));
	}
}
