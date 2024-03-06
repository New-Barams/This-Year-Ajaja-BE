package me.ajaja.module.tag.application;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.tag.domain.PlanTagRepository;
import me.ajaja.module.tag.domain.PlanTag;
import me.ajaja.module.tag.domain.Tag;
import me.ajaja.module.tag.domain.TagRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CreatePlanTagService {
	private final TagRepository tagRepository;
	private final PlanTagRepository planTagRepository;

	public List<String> create(Long planId, List<String> tagNames) {
		if (tagNames == null) {
			return null;
		}

		Set<String> tagNameSet = new LinkedHashSet<>(tagNames);

		return tagNameSet.stream()
			.map(tagName -> savePlanTag(planId, tagName))
			.toList();
	}

	private String savePlanTag(Long planId, String tagName) {
		Tag tag = getOrCreateTagIfNotExists(tagName);

		PlanTag planTag = new PlanTag(planId, tag.getId());
		planTagRepository.save(planTag);

		return tag.getName();
	}

	private Tag getOrCreateTagIfNotExists(String name) {
		return tagRepository.findByName(name)
			.orElseGet(() -> tagRepository.save(new Tag(name)));
	}
}
