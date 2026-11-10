package com.devsuperior.validationsecurity.city.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.validationsecurity.city.dto.CityDTO;
import com.devsuperior.validationsecurity.city.entity.City;
import com.devsuperior.validationsecurity.city.repository.CityRepository;

@Service
public class CityService {

	private final CityRepository cityRepository;

	public CityService(CityRepository cityRepository) {
		this.cityRepository = cityRepository;
	}

	@Transactional(readOnly = true)
	public List<CityDTO> list() {
		return cityRepository.listAll();
	}

	@Transactional
	public CityDTO create(CityDTO cityDTO) {
		City savedCity = cityRepository.save(new City(cityDTO.name()));
		return CityDTO.from(savedCity);
	}

}
