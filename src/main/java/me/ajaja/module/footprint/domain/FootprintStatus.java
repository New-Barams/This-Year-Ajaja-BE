package me.ajaja.module.footprint.domain;

import lombok.Getter;

@Getter
public class FootprintStatus {
	private boolean isPublic;
	private boolean deleted;

	public FootprintStatus(boolean isPublic) {
		this.isPublic = isPublic;
		this.deleted = false;
	}

	void switchPublic() {
		this.isPublic = !isPublic;
	}

	void toDeleted() {
		this.deleted = true;
	}
}
