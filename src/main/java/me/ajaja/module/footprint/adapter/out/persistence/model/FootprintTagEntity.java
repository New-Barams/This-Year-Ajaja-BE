// package me.ajaja.module.footprint.adapter.out.persistence.model;
//
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import lombok.AccessLevel;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import me.ajaja.module.tag.domain.Tag;
//
// @Getter
// @Entity
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
// @AllArgsConstructor
// public class FootprintTagEntity {
// 	@Id
// 	@GeneratedValue(strategy = GenerationType.IDENTITY)
// 	@Column(name = "footprint_tag_id")
// 	private Long id;
//
// 	@ManyToOne
// 	@JoinColumn(name = "footprint_id")
// 	private FootprintEntity footprint;
//
// 	@ManyToOne
// 	@JoinColumn(name = "tag_id")
// 	private Tag tag;
// }
