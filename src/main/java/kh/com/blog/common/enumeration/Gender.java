package kh.com.blog.common.enumeration;

import lombok.Getter;

@Getter
public enum Gender {
	MALE("MALE"),
	FEMALE("FEMALE"),
	PREFER_NOT_TO_SAY("PREFER_NOT_TO_SAY");
	private final String value;

	private Gender(String value) {
		this.value = value;
	}

}
