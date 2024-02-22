package me.ajaja.module.ajaja.infra;

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

@Entity
@Getter
@Table(name = "ajajas")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AjajaEntity extends BaseEntity<AjajaEntity> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ajaja_id")
	private Long id;

	@Column(name = "target_id", nullable = false)
	private Long targetId;

	@Column(nullable = false)
	private Long userId;

	@Column(name = "is_canceled", nullable = false)
	private boolean canceled;

	@Column(name = "target_type", nullable = false)
	private String type;
}
