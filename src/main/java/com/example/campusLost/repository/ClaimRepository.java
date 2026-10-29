package com.example.campusLost.repository;

import com.example.campusLost.entity.ClaimRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClaimRepository extends JpaRepository<ClaimRequest, Long> {
    List<ClaimRequest> findByItemId(Long itemId);
}