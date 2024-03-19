package me.ajaja.module.footprint.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;

import me.ajaja.module.footprint.adapter.out.persistence.model.FootprintEntity;
import me.ajaja.module.footprint.domain.AjajaFootprint;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.domain.FreeFootprint;
import me.ajaja.module.footprint.domain.Target;
import me.ajaja.module.footprint.domain.Title;
import me.ajaja.module.footprint.domain.Writer;
import me.ajaja.module.footprint.dto.Entity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FootprintMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(source = "target.id", target = "targetId")
	@Mapping(source = "writer.id", target = "writerId")
	@Mapping(source = "iconNumber", target = "iconNumber")
	@Mapping(source = "title.title", target = "title")
	@Mapping(source = "visible", target = "visible")
	@Mapping(source = "deleted", target = "deleted")
	@Mapping(target = "type", expression = "java(toType(footprint))")
	@Mapping(target = "content", expression = "java(toContent(footprint))")
	@Mapping(target = "emotion", expression = "java(toEmotion(footprint))")
	@Mapping(target = "reason", expression = "java(toReason(footprint))")
	@Mapping(target = "strengths", expression = "java(toStrengths(footprint))")
	@Mapping(target = "weaknesses", expression = "java(toWeaknesses(footprint))")
	@Mapping(target = "postScript", expression = "java(toPostScript(footprint))")
	FootprintEntity toEntity(Footprint footprint);

	default String toType(Footprint footprint) {
		return (footprint instanceof FreeFootprint) ? "FREE" : "KPT";
	}

	default String toContent(Footprint footprint) {
		return (footprint instanceof FreeFootprint) ? ((FreeFootprint)footprint).getContent() : null;
	}

	default String toEmotion(Footprint footprint) {
		return (footprint instanceof AjajaFootprint) ? ((AjajaFootprint)footprint).getEmotion() : null;
	}

	default String toReason(Footprint footprint) {
		return (footprint instanceof AjajaFootprint) ? ((AjajaFootprint)footprint).getReason() : null;
	}

	default String toStrengths(Footprint footprint) {
		return (footprint instanceof AjajaFootprint) ? ((AjajaFootprint)footprint).getStrengths() : null;
	}

	default String toWeaknesses(Footprint footprint) {
		return (footprint instanceof AjajaFootprint) ? ((AjajaFootprint)footprint).getWeaknesses() : null;
	}

	default String toPostScript(Footprint footprint) {
		return (footprint instanceof AjajaFootprint) ? ((AjajaFootprint)footprint).getPostScript() : null;
	}

	Footprint toDomain(FootprintEntity footprintEntity, Entity.Target target, Entity.Writer writer);

	@ObjectFactory
	default Footprint createFootprint(FootprintEntity footprintEntity, Entity.Target target, Entity.Writer writer) {
		String type = footprintEntity.getType();
		if (type.equals("FREE")) {
			return new FreeFootprint(
				footprintEntity.getId(),
				new Target(target.getId(), target.getTitle()),
				new Writer(writer.getId(), writer.getNickname()),
				Footprint.Type.FREE,
				footprintEntity.getIconNumber(),
				new Title(footprintEntity.getTitle()),
				footprintEntity.isVisible(),
				footprintEntity.isDeleted(),
				footprintEntity.getContent()
			);
		} else {
			return new AjajaFootprint(
				footprintEntity.getId(),
				new Target(target.getId(), target.getTitle()),
				new Writer(writer.getId(), writer.getNickname()),
				Footprint.Type.AJAJA,
				footprintEntity.getIconNumber(),
				new Title(footprintEntity.getTitle()),
				footprintEntity.isVisible(),
				footprintEntity.isDeleted(),
				footprintEntity.getEmotion(),
				footprintEntity.getReason(),
				footprintEntity.getStrengths(),
				footprintEntity.getWeaknesses(),
				footprintEntity.getPostScript()
			);
		}
	}
}
