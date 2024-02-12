package me.ajaja.module.footprint.domain;

import lombok.Getter;

@Getter
public class FootprintStatus {
	private boolean visible;
	private boolean deleted;

	public FootprintStatus(boolean visible) {
		this.visible = visible;
		this.deleted = false;
	}

	void switchVisibility() {
		this.visible = !visible;
	}

	void delete() {
		this.deleted = true;
	}
}
