package com.devsuperior.validationsecurity.city.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.validationsecurity.city.dto.CityDTO;
import com.devsuperior.validationsecurity.city.service.CityService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cities")
public class CityController {

	private final CityService cityService;

	public CityController(CityService cityService) {
		this.cityService = cityService;
	}

	@GetMapping
	ResponseEntity<List<CityDTO>> list() {
		List<CityDTO> cities = cityService.list();
		return ResponseEntity.ok(cities);
	}

	@PostMapping
	ResponseEntity<CityDTO> create(@Valid @RequestBody CityDTO cityDTO) {
		CityDTO createdCityDTO = cityService.create(cityDTO);
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{cityId}")
				.buildAndExpand(createdCityDTO.cityId())
				.toUri();

		return ResponseEntity.created(uri).body(createdCityDTO);
	}

}
