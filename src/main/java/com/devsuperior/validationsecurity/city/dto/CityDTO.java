package com.devsuperior.validationsecurity.city.dto;

import com.devsuperior.validationsecurity.city.entity.City;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public record CityDTO(
		@JsonProperty("id")
		Long cityId,
		@NotBlank(message = "Field is required")
		String name) {

	public static CityDTO from(City city) {
		return new CityDTO(city.getCityId(), city.getName());
	}

}
