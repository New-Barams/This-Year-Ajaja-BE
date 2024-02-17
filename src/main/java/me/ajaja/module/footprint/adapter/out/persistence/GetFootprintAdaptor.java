package me.ajaja.module.footprint.adapter.out.persistence;

import static me.ajaja.global.exception.ErrorCode.*;
import static me.ajaja.module.plan.adapter.out.persistence.model.QPlanEntity.*;
import static me.ajaja.module.user.adapter.out.persistence.model.QUserEntity.*;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.footprint.adapter.out.persistence.model.FootprintEntity;
import me.ajaja.module.footprint.adapter.out.persistence.model.TargetEntity;
import me.ajaja.module.footprint.adapter.out.persistence.model.WriterEntity;
import me.ajaja.module.footprint.application.port.out.GetFootprintPort;
import me.ajaja.module.footprint.domain.Footprint;
import me.ajaja.module.footprint.mapper.FootprintMapper;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetFootprintAdaptor implements GetFootprintPort {
	private final FootprintJpaRepository footprintJpaRepository;
	private final JPAQueryFactory queryFactory;
	private final FootprintMapper footprintMapper;

	@Override
	public Footprint getFootprint(Long id) {
		FootprintEntity footprintEntity = footprintJpaRepository.findById(id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_FOOTPRINT));

		TargetEntity targetEntity = queryFactory.select(
				Projections.fields(TargetEntity.class, planEntity.id, planEntity.title))
			.from(planEntity)
			.where(planEntity.id.eq(footprintEntity.getTargetId()))
			.fetchOne();

		WriterEntity writerEntity = queryFactory.select(
				Projections.fields(WriterEntity.class, userEntity.id, userEntity.nickname))
			.from(userEntity)
			.where(userEntity.id.eq(footprintEntity.getWriterId()))
			.fetchOne();

		Footprint footprint = footprintMapper.toDomain(footprintEntity, targetEntity, writerEntity);

		return footprint;
	}
}
