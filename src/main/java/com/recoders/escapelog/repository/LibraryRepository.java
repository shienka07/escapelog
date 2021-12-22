package com.recoders.escapelog.repository;

import com.recoders.escapelog.domain.Recode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LibraryRepository extends JpaRepository<Recode, Long> {

    List<Recode> findByMember_LibraryNameOrderByRegdateDesc(String libraryName);

    Optional<Recode> deleteByNo(Long no);

    List<Recode> findByMember_LibraryNameAndTheme_ThemeNameContaining(String libraryName, String keyword);

    Optional<Recode> findByNo(Long no);
    
    @Query("SELECT COUNT(r.no) FROM Recode r WHERE r.theme.no = :themeNo AND r.rating = :rating")
    int countRecodeRating(@Param("themeNo")Long no, @Param("rating") Integer rating);

    List<Recode> findByThemeNoOrderByRegdateDesc(Long themeNo);

    List<Recode> findByThemeNoAndRatingOrderByRegdateDesc(Long themeNo, Integer rating);

}