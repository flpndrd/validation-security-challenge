package com.devsuperior.validationsecurity.event.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.validationsecurity.city.entity.City;
import com.devsuperior.validationsecurity.city.repository.CityRepository;
import com.devsuperior.validationsecurity.event.dto.EventDTO;
import com.devsuperior.validationsecurity.event.entity.Event;
import com.devsuperior.validationsecurity.event.exception.InvalidEventCityException;
import com.devsuperior.validationsecurity.event.repository.EventRepository;

@Service
public class EventService {

	private final EventRepository eventRepository;

	private final CityRepository cityRepository;

	public EventService(EventRepository eventRepository, CityRepository cityRepository) {
		this.eventRepository = eventRepository;
		this.cityRepository = cityRepository;
	}

	@Transactional(readOnly = true)
	public Page<EventDTO> list(Pageable pageable) {
		return eventRepository.listAll(pageable);
	}

	@Transactional
	public EventDTO create(EventDTO eventDTO) {
		City city = cityRepository.findById(eventDTO.cityId()).orElseThrow(() -> new InvalidEventCityException());
		Event event = new Event(eventDTO.name(), eventDTO.date(), eventDTO.url(), city);
		Event savedEvent = eventRepository.save(event);
		return EventDTO.from(savedEvent);
	}

}
