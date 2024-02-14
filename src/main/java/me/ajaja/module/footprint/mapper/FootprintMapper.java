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
import me.ajaja.module.footprint.adapter.out.persistence.model.FootprintEntity;
import me.ajaja.module.footprint.adapter.out.persistence.model.Tag;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.Target;
import me.ajaja.module.footprint.domain.Title;
import me.ajaja.module.footprint.domain.Writer;

@Mapper(componentModel = "spring")
public interface FootprintMapper {

	@Retention(RetentionPolicy.CLASS)
	@Mapping(target = "id", ignore = true)
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

	@Retention(RetentionPolicy.CLASS)
	@Mapping(source = "id", target = "id")
	@Mapping(source = "footprintEntity", target = "target", qualifiedByName = "toTarget")
	@Mapping(source = "footprintEntity", target = "writer", qualifiedByName = "toWriter")
	@Mapping(source = "footprintEntity", target = "title", qualifiedByName = "toTitle")
	@Mapping(source = "visible", target = "visible")
	@Mapping(source = "deleted", target = "deleted")
	@Mapping(source = "footprintEntity", target = "tags", qualifiedByName = "toTags")
	@Mapping(source = "footprintEntity", target = "ajajas", qualifiedByName = "toAjajas")
	@interface toDomain {
	}

	@Named("toTarget")
	static Target toTarget(FootprintEntity footprintEntity) {
		return new Target(footprintEntity.getTargetId(), footprintEntity.getTitle());
	}

	@Named("toWriter")
	static Writer toWriter(FootprintEntity footprintEntity) {
		return new Writer(footprintEntity.getWriterId(), footprintEntity.getNickname());
	}

	@Named("toTitle")
	static Title toTitle(FootprintEntity footprintEntity) {
		return new Title(footprintEntity.getTitle());
	}

	@Named("toTags")
	static Set<me.ajaja.module.footprint.domain.Tag> toTags(FootprintEntity footprintEntity) {
		return footprintEntity.getTags()
			.stream()
			.map(tag -> new me.ajaja.module.footprint.domain.Tag(tag.getId(), tag.getName()))
			.collect(Collectors.toSet());
	}

	@Named("toAjajas")
	static List<me.ajaja.module.footprint.domain.Ajaja> toAjajas(FootprintEntity footprintEntity) {
		return footprintEntity.getAjajas()
			.stream()
			.map(ajaja -> new me.ajaja.module.footprint.domain.Ajaja(ajaja.getId()))
			.toList();
	}
}
