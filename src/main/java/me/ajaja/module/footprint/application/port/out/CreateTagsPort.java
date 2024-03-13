package me.ajaja.module.footprint.application.port.out;

import java.util.List;

public interface CreateTagsPort {
	/**
	 * 발자취 태그 생성 외부 요청 포트
	 * @param footprintId 발자취 식별자
	 * @param tags 태그 이름
	 */
	void create(Long footprintId, List<String> tags);
}
