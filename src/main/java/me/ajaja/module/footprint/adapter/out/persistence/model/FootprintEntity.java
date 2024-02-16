package me.ajaja.module.footprint.adapter.out.persistence.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Where;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ajaja.global.common.BaseEntity;
import me.ajaja.module.ajaja.infra.AjajaEntity;

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

	@OneToMany(mappedBy = "footprint", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<FootprintTagEntity> footprintTags = new HashSet<>();

	@Where(clause = "target_type = \"FOOTPRINT\"")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "target_id")
	private List<AjajaEntity> ajajas = new ArrayList<>();

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(columnDefinition = "TEXT")
	private String keepContent;

	@Column(columnDefinition = "TEXT")
	private String problemContent;

	@Column(columnDefinition = "TEXT")
	private String tryContent;
}
