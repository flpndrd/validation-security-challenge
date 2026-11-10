package com.devsuperior.validationsecurity.event.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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
import com.devsuperior.validationsecurity.event.dto.EventDTO;

import tools.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIT {

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
		EventDTO dto = new EventDTO(null, "Expo XP", LocalDate.of(2021, 5, 18), "https://expoxp.com.br", 1L);
		String jsonBody = objectMapper.writeValueAsString(dto);

		ResultActions result = mockMvc.perform(post("/events")
				.header("Authorization", "Bearer " + invalidToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());
	}

	@Test
	void insertShouldInsertResourceWhenClientLoggedAndCorrectData() throws Exception {
		LocalDate nextMonth = LocalDate.now().plusMonths(1L);
		EventDTO dto = new EventDTO(null, "Expo XP", nextMonth, "https://expoxp.com.br", 1L);
		String jsonBody = objectMapper.writeValueAsString(dto);

		ResultActions result = mockMvc.perform(post("/events")
				.header("Authorization", "Bearer " + clientToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("Expo XP"));
		result.andExpect(jsonPath("$.date").value(nextMonth.toString()));
		result.andExpect(jsonPath("$.url").value("https://expoxp.com.br"));
		result.andExpect(jsonPath("$.cityId").value(1L));
	}

	@Test
	void insertShouldInsertResourceWhenAdminLoggedAndCorrectData() throws Exception {
		LocalDate nextMonth = LocalDate.now().plusMonths(1L);
		EventDTO dto = new EventDTO(null, "Expo XP", nextMonth, "https://expoxp.com.br", 1L);
		String jsonBody = objectMapper.writeValueAsString(dto);

		ResultActions result = mockMvc.perform(post("/events")
				.header("Authorization", "Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("Expo XP"));
		result.andExpect(jsonPath("$.date").value(nextMonth.toString()));
		result.andExpect(jsonPath("$.url").value("https://expoxp.com.br"));
		result.andExpect(jsonPath("$.cityId").value(1L));
	}

	@Test
	void insertShouldReturn422WhenAdminLoggedAndBlankName() throws Exception {
		LocalDate nextMonth = LocalDate.now().plusMonths(1L);
		EventDTO dto = new EventDTO(null, "      ", nextMonth, "https://expoxp.com.br", 1L);
		String jsonBody = objectMapper.writeValueAsString(dto);

		ResultActions result = mockMvc.perform(post("/events")
				.header("Authorization", "Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnprocessableContent());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("name"));
		result.andExpect(jsonPath("$.errors[0].message").value("Field is required"));
	}

	@Test
	void insertShouldReturn422WhenAdminLoggedAndPastDate() throws Exception {
		LocalDate pastMonth = LocalDate.now().minusMonths(1L);
		EventDTO dto = new EventDTO(null, "Expo XP", pastMonth, "https://expoxp.com.br", 1L);
		String jsonBody = objectMapper.writeValueAsString(dto);

		ResultActions result = mockMvc.perform(post("/events")
				.header("Authorization", "Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnprocessableContent());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("date"));
		result.andExpect(jsonPath("$.errors[0].message").value("Event date cannot be a past date"));
	}

	@Test
	void insertShouldReturn422WhenAdminLoggedAndNullCity() throws Exception {
		LocalDate nextMonth = LocalDate.now().plusMonths(1L);
		EventDTO dto = new EventDTO(null, "Expo XP", nextMonth, "https://expoxp.com.br", null);
		String jsonBody = objectMapper.writeValueAsString(dto);

		ResultActions result = mockMvc.perform(post("/events")
				.header("Authorization", "Bearer " + adminToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnprocessableContent());
		result.andExpect(jsonPath("$.errors[0].fieldName").value("cityId"));
		result.andExpect(jsonPath("$.errors[0].message").value("Field is required"));
	}

	@Test
	void findAllShouldReturnPagedResources() throws Exception {
		ResultActions result = mockMvc.perform(get("/events").contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
	}

}
