package me.ajaja.module.tag.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ajaja.global.common.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanTag extends BaseEntity<PlanTag> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "plan_tag_id")
	private Long id;

	@NotNull
	private Long planId;

	@NotNull
	private Long tagId;

	public PlanTag(Long planId, Long tagId) {
		this.planId = planId;
		this.tagId = tagId;
		this.validateSelf();
	}
}
