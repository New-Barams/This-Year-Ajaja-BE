package com.newbarams.ajaja.module.plan.dto;

import java.util.List;

import org.ahocorasick.trie.Emit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BanWordValidationResult {
	private final String originSentence;
	private final List<Emit> badWordResults;
}
