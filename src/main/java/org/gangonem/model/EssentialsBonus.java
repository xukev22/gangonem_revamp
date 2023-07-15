package org.gangonem.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EssentialsBonus {

	String name;
	String nickname;
	String town;
	String hexColor;

	@JsonCreator
	public EssentialsBonus(@JsonProperty("name") String name, @JsonProperty("nickname") String nickname,
			@JsonProperty("town") String town, @JsonProperty("colors") String hexColor) {
		this.name = name;
		this.nickname = nickname;
		this.town = town;
		this.hexColor = hexColor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EssentialsBonus that = (EssentialsBonus) o;
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	public EssentialsBonus() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getHexColor() {
		return hexColor;
	}

	public void setHexColor(String hexColor) {
		this.hexColor = hexColor;
	}
	
	

}
