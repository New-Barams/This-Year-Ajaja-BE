package me.ajaja.module.footprint.adapter.out.persistence.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;
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

	@Column(name = "footprint_type", nullable = false, length = 10)
	private String type;

	@Column(nullable = false)
	private Long targetId;

	@Column(nullable = false, length = 50)
	private String targetTitle;

	@Column(nullable = false)
	private Long writerId;

	@Column(nullable = false, length = 20)
	private String nickname;

	@Column(nullable = false, length = 50)
	private String title;

	@Column(nullable = false)
	private boolean visible;

	@Column(nullable = false)
	@ColumnDefault(value = "false")
	private boolean deleted;

	@ElementCollection
	@CollectionTable(name = "footprint_tags", joinColumns = @JoinColumn(name = "footprint_id"))
	private Set<Tag> tags = new HashSet<>();

	@ElementCollection
	@CollectionTable(name = "footprint_ajajas", joinColumns = @JoinColumn(name = "footprint_id"))
	private List<Ajaja> ajajas = new ArrayList<>();

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(columnDefinition = "TEXT")
	private String keepContent;

	@Column(columnDefinition = "TEXT")
	private String problemContent;

	@Column(columnDefinition = "TEXT")
	private String tryContent;
}
