package me.ajaja.module.tag.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ajaja.global.common.BaseEntity;

@Getter
@Entity
@Table(name = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseEntity<Tag> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tag_id")
	private Long id;

	@NotBlank
	@Size(max = 10)
	@Column(name = "tag_name", unique = true)
	private String name;

	public Tag(String name) {
		this.name = name;
		this.validateSelf();
	}
}
