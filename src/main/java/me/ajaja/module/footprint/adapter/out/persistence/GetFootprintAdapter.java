package me.ajaja.module.footprint.adapter.out.persistence;

import static me.ajaja.module.footprint.adapter.out.persistence.model.QFootprintEntity.*;
import static me.ajaja.module.plan.adapter.out.persistence.model.QPlanEntity.*;
import static me.ajaja.module.user.adapter.out.persistence.model.QUserEntity.*;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.footprint.adapter.out.persistence.model.FootprintEntity;
import me.ajaja.module.footprint.application.port.out.GetFootprintPort;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.dto.Entity;
import me.ajaja.module.footprint.dto.QEntity_Target;
import me.ajaja.module.footprint.dto.QEntity_Writer;
import me.ajaja.module.footprint.mapper.FootprintMapper;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetFootprintAdapter implements GetFootprintPort {
	private final JPAQueryFactory queryFactory;
	private final FootprintMapper footprintMapper;

	@Override
	public Footprint getFootprint(Long id) {
		Tuple tuple = queryFactory.select(footprintEntity, new QEntity_Target(planEntity.id, planEntity.title),
				new QEntity_Writer(userEntity.id, userEntity.nickname))
			.from(footprintEntity)
			.leftJoin(planEntity)
			.on(planEntity.id.eq(footprintEntity.targetId))
			.leftJoin(userEntity)
			.on(userEntity.id.eq(footprintEntity.writerId))
			.where(footprintEntity.id.eq(id)).fetchOne();

		FootprintEntity footprint = tuple.get(footprintEntity);
		Entity.Target target = tuple.get(new QEntity_Target(planEntity.id, planEntity.title));
		Entity.Writer writer = tuple.get(new QEntity_Writer(userEntity.id, userEntity.nickname));

		return footprintMapper.toDomain(footprint, target, writer);
	}
}
