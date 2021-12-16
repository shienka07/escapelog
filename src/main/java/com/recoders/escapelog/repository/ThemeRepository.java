package com.recoders.escapelog.repository;

import com.recoders.escapelog.domain.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long>, ThemeRepositoryCustom {

}
