package com.example.backend.dto.admin;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AdminTimeseriesPoint {
	String label;
	Double completionRate;
	Long totalBookings;
}
