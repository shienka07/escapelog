package com.recoders.escapelog.repository;

import com.recoders.escapelog.domain.Recode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<Recode, Long> {

    List<Recode> findByMember_LibraryNameOrderByRegdateDesc(String libraryName);

    Optional<Recode> deleteByNo(Long no);

    List<Recode> findByMember_LibraryNameAndTheme_ThemeNameContaining(String libraryName, String keyword);




}