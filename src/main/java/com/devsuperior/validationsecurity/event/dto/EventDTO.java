package com.devsuperior.validationsecurity.event.dto;

import java.time.LocalDate;

import com.devsuperior.validationsecurity.event.entity.Event;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventDTO(
		@JsonProperty("id")
		Long eventId,
		@NotBlank(message = "Field is required")
		String name,
		@FutureOrPresent(message = "Event date cannot be a past date")
		LocalDate date,
		String url,
		@NotNull(message = "Field is required")
		Long cityId) {

	public static EventDTO from(Event event) {
		return new EventDTO(
				event.getEventId(),
				event.getName(),
				event.getDate(),
				event.getUrl(),
				event.getCity().getCityId());
	}

}
