package com.example.backend.dto.admin;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AdminPieSlice {
	String name;
	Double value;
}
