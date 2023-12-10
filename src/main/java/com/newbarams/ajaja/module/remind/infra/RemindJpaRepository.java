package com.newbarams.ajaja.module.remind.infra;

import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.repository.JpaRepository;

import com.newbarams.ajaja.module.remind.domain.Remind;
import com.newbarams.ajaja.module.remind.mapper.RemindEntityMapper;

public interface RemindJpaRepository extends JpaRepository<RemindEntity, Long> {
	RemindEntityMapper mapper = Mappers.getMapper(RemindEntityMapper.class);

	default void save(Remind remind) {
		save(mapper.mapEntityFrom(remind));
	}
}
