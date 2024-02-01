package com.newbarams.ajaja.module.plan.domain;

import java.util.Collection;

import org.ahocorasick.trie.Emit;

public class BanWordFilter {
	public static Collection<Emit> validate(String originSentence) {
		return WordBundle.banWord.parseText(originSentence);
	}
}
