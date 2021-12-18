package com.recoders.escapelog.repository;

import com.recoders.escapelog.domain.Recode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LibraryRepository extends JpaRepository<Recode, Long> {

    List<Recode> findAll();

    List<Recode> findByMember_NicknameOrderByRegdateDesc(String nickname);
    // select count(rating) from recode where theme.no = no and rating = num;

    @Query("SELECT COUNT(r.no) FROM Recode r WHERE r.theme.no = :themeNo AND r.rating = :rating")
    Integer countRecodeRating(@Param("themeNo")Long no, @Param("rating") Integer rating);

    List<Recode> findByThemeNoOrderByRegdateDesc(Long themeNo);

    List<Recode> findByThemeNoAndRatingOrderByRegdateDesc(Long themeNo, Integer rating);
}