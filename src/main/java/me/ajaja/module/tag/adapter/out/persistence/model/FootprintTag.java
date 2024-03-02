package me.ajaja.module.tag.adapter.out.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "footprint_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FootprintTag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "footprint_tag_id")
	private Long id;

	@Column(name = "footprint_id", nullable = false)
	private Long footprintId;

	@Column(name = "tag_id", nullable = false)
	private Long tagId;

	public FootprintTag(Long footprintId, Long tagId) {
		this.footprintId = footprintId;
		this.tagId = tagId;
	}
}
