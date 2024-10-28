package com.baharat.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {

	@JsonProperty("username")
	private String username;

	@JsonProperty("password")
	private String password;
}
