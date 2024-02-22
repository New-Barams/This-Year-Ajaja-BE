package me.ajaja.module.plan.domain;

import java.util.List;

import org.ahocorasick.trie.Emit;

public class BanWordFilter {
	public static List<String> validate(String originSentence) {

		return WordBundle.banWord.parseText(originSentence).stream()
			.map(Emit::getKeyword)
			.toList();
	}
}
