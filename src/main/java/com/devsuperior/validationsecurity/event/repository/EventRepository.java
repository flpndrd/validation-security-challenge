package com.devsuperior.validationsecurity.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.validationsecurity.event.dto.EventDTO;
import com.devsuperior.validationsecurity.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

	@Query("""
			SELECT new com.devsuperior.validationsecurity.event.dto.EventDTO(
				event.eventId,
				event.name,
				event.date,
				event.url,
				event.city.cityId)
			FROM Event event
			""")
	Page<EventDTO> listAll(Pageable pageable);

}
