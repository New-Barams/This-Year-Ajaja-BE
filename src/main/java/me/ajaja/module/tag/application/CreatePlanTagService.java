package me.ajaja.module.tag.application;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.tag.adapter.out.persistence.PlanTagRepository;
import me.ajaja.module.tag.adapter.out.persistence.TagRepository;
import me.ajaja.module.tag.adapter.out.persistence.model.PlanTag;
import me.ajaja.module.tag.adapter.out.persistence.model.Tag;
import me.ajaja.module.tag.application.port.out.CreateTagPort;

@Service
@Qualifier("plan")
@Transactional
@RequiredArgsConstructor
public class CreatePlanTagService implements CreateTagPort {
	private final TagRepository tagRepository;
	private final PlanTagRepository planTagRepository;

	@Override
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
