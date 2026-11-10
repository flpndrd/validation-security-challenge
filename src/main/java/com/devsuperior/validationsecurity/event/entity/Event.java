package com.devsuperior.validationsecurity.event.entity;

import java.time.LocalDate;

import com.devsuperior.validationsecurity.city.entity.City;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_event")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long eventId;

	private String name;

	private LocalDate date;

	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id")
	private City city;

	protected Event() {
	}

	public Event(String name, LocalDate date, String url, City city) {
		this.name = name;
		this.date = date;
		this.url = url;
		this.city = city;
	}

	public Long getEventId() {
		return eventId;
	}

	public String getName() {
		return name;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getUrl() {
		return url;
	}

	public City getCity() {
		return city;
	}

}
