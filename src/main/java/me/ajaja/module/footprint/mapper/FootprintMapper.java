package me.ajaja.module.footprint.mapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import me.ajaja.module.footprint.adapter.out.persistence.model.Ajaja;
import me.ajaja.module.footprint.adapter.out.persistence.model.Tag;
import me.ajaja.module.footprint.domain.Footprint;

@Mapper(componentModel = "spring")
public interface FootprintMapper {

	@Retention(RetentionPolicy.CLASS)
	@Mapping(source = "target.id", target = "targetId")
	@Mapping(source = "target.title", target = "targetTitle")
	@Mapping(source = "writer.id", target = "writerId")
	@Mapping(source = "writer.nickname", target = "nickname")
	@Mapping(source = "title.title", target = "title")
	@Mapping(source = "visible", target = "visible")
	@Mapping(source = "deleted", target = "deleted")
	@Mapping(source = "footprint", target = "tags", qualifiedByName = "toTags")
	@Mapping(source = "footprint", target = "ajajas", qualifiedByName = "toAjajas")
	@interface ToEntity {
	}

	@Named("toTags")
	static Set<Tag> toTags(Footprint footprint) {
		return footprint.getTags().stream().map(tag -> new Tag(tag.getId(), tag.getName())).collect(Collectors.toSet());
	}

	@Named("toAjajas")
	static List<Ajaja> toAjajas(Footprint footprint) {
		return footprint.getAjajas()
			.stream()
			.map(ajaja -> new me.ajaja.module.footprint.adapter.out.persistence.model.Ajaja(ajaja.getId()))
			.toList();
	}
}
