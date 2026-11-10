package com.devsuperior.validationsecurity.city.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_city")
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cityId;

	private String name;

	protected City() {
	}

	public City(String name) {
		this.name = name;
	}

	public Long getCityId() {
		return cityId;
	}

	public String getName() {
		return name;
	}

}
