package me.ajaja.module.tag.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "footprint_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FootprintTag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "footprint_tag_id")
	private Long id;

	@Column(name = "tag_id")
	@NotNull
	private Long tagId;

	@Column(name = "footprint_id")
	@NotNull
	private Long footprintId;

	public FootprintTag(Long id, Long tagId, Long footprintId) {
		this.id = id;
		this.tagId = tagId;
		this.footprintId = footprintId;
	}
}
