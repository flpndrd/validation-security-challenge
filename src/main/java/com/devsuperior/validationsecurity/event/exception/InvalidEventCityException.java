package com.devsuperior.validationsecurity.event.exception;

import java.io.Serial;

import com.devsuperior.validationsecurity.exception.InvalidRequestException;

public class InvalidEventCityException extends InvalidRequestException {

	@Serial
	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "Event has a city that does not exist.";

	public InvalidEventCityException() {
		super(MESSAGE);
	}

}
