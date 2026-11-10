package com.devsuperior.validationsecurity.event.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.validationsecurity.event.dto.EventDTO;
import com.devsuperior.validationsecurity.event.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/events")
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@GetMapping
	ResponseEntity<Page<EventDTO>> list(Pageable pageable) {
		Page<EventDTO> eventPage = eventService.list(pageable);
		return ResponseEntity.ok(eventPage);
	}

	@PostMapping
	ResponseEntity<EventDTO> create(@Valid @RequestBody EventDTO eventDTO) {
		EventDTO createdEventDTO = eventService.create(eventDTO);
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{eventId}")
				.buildAndExpand(createdEventDTO.eventId())
				.toUri();

		return ResponseEntity.created(uri).body(createdEventDTO);
	}

}
