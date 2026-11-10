package com.devsuperior.validationsecurity.city.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.validationsecurity.accesstoken.AccessTokenSupport;
import com.devsuperior.validationsecurity.city.dto.CityDTO;

import tools.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CityControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AccessTokenSupport accessTokenSupport;

	private String clientUsername;

	private String clientPassword;

	private String adminUsername;

	private String adminPassword;

	private String clientToken;

	private String adminToken;

	private String invalidToken;

	@BeforeEach
	void setUp() throws Exception {
		clientUsername = "ana@gmail.com";
		clientPassword = "123456";
		adminUsername = "bob@gmail.com";
		adminPassword = "123456";
		clientToken = accessTokenSupport.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		adminToken = accessTokenSupport.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		invalidToken = adminToken + "xpto";
	}

	@Test
	void insertShouldReturn401WhenInvalidToken() throws Exception {
		CityDTO dto = new CityDTO(null, "Recife");
		String jsonBody = objectMapper.writeValueAsString(dto);

		ResultActions result = mockMvc.perform(post("/cities")
				.header("Authorization", "Bearer " + invalidToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());
	}

	@Test
	void insertShouldReturn403WhenClientLogged() throws Exception {
		CityDTO dto = new CityDTO(null, "Recife");
		String jsonBody = objectMapper.writeValueAsString(dto);

		ResultActions result = mockMvc.perform(post("/cities")
				.header("Authorization", "Bearer " + clientToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());
	}

	@Test
	void insertShouldInsertResourceWhenAdminLoggedAndCorrectData() throws Exception {
		CityDTO dto = new CityDTO(null, "Recife");
		String jsonBody = objectMapper.writeValueAsString(dto);

		ResultActions result = mockMvc.perform(post("/cities")
				.header("Authorization", "Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("Recife"));
	}

	@Test
	void insertShouldReturn422WhenAdminLoggedAndBlankName() throws Exception {
		CityDTO dto = new CityDTO(null, "    ");
		String jsonBody = objectMapper.writeValueAsString(dto);

		ResultActions result = mockMvc.perform(post("/cities")
				.header("Authorization", "Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnprocessableContent());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("name"));
		result.andExpect(jsonPath("$.errors[0].message").value("Field is required"));
	}

	@Test
	void findAllShouldReturnAllResourcesSortedByName() throws Exception {
		ResultActions result = mockMvc.perform(get("/cities").contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$[0].name").value("Belo Horizonte"));
		result.andExpect(jsonPath("$[1].name").value("Belém"));
		result.andExpect(jsonPath("$[2].name").value("Brasília"));
	}

}
