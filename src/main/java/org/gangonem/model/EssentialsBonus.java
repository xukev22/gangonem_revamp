package org.gangonem.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EssentialsBonus {

	String name;
	String nickname;
	String town;
	List<String> colors;

	@JsonCreator
	public EssentialsBonus(@JsonProperty("name") String name, @JsonProperty("nickname") String nickname,
			@JsonProperty("town") String town, @JsonProperty("colors") List<String> colors) {
		this.name = name;
		this.nickname = nickname;
		this.town = town;
		this.colors = colors;
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

	public List<String> getColors() {
		return colors;
	}

	public void setColors(List<String> colors) {
		this.colors = colors;
	}

}
