package com.devsuperior.validationsecurity.city.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.validationsecurity.city.dto.CityDTO;
import com.devsuperior.validationsecurity.city.entity.City;

public interface CityRepository extends JpaRepository<City, Long> {

	@Query("""
			SELECT new com.devsuperior.validationsecurity.city.dto.CityDTO(city.cityId, city.name)
			FROM City city
			ORDER BY city.name
			""")
	List<CityDTO> listAll();

}
