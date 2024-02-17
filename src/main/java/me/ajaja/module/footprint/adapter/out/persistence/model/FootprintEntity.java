package me.ajaja.module.footprint.adapter.out.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ajaja.global.common.BaseEntity;

@Getter
@Entity
@Table(name = "footprints")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FootprintEntity extends BaseEntity<FootprintEntity> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "footprint_id")
	private Long id;

	@Column(nullable = false)
	private Long targetId;

	@Column(nullable = false)
	private Long writerId;

	@Column(name = "footprint_type", nullable = false, length = 10)
	private String type;

	@Column(nullable = false, length = 50)
	private String title;

	@Column(nullable = false)
	private boolean visible;

	@Column(nullable = false)
	private boolean deleted;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(columnDefinition = "TEXT")
	private String keepContent;

	@Column(columnDefinition = "TEXT")
	private String problemContent;

	@Column(columnDefinition = "TEXT")
	private String tryContent;
}
