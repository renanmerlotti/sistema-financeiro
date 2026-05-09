package com.financas.backend.service;

import com.financas.backend.dto.response.DashboardResponseDTO;

public interface DashboardService {
    DashboardResponseDTO getDashboard(Long userId, String period);
}
