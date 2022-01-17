package com.recoders.escapelog.repository;

import com.recoders.escapelog.domain.Recode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LibraryRepository extends JpaRepository<Recode, Long> {

    Page<Recode> findByMember_LibraryNameOrderByRegdateDesc(String libraryName, Pageable pageable);

    Optional<Recode> deleteByNo(Long no);

    Page<Recode> findByMember_LibraryNameAndTheme_ThemeNameContaining(String libraryName, String keyword, Pageable pageable);

    Optional<Recode> findByNo(Long no);
    
    @Query("SELECT COUNT(r.no) FROM Recode r WHERE r.theme.no = :themeNo AND r.rating = :rating")
    int countRecodeRating(@Param("themeNo")Long no, @Param("rating") Integer rating);

    int countRecodeByThemeNo(Long themeNo);

    List<Recode> findByThemeNoOrderByRegdateDesc(Long themeNo);

    List<Recode> findByThemeNoAndRatingOrderByRegdateDesc(Long themeNo, Integer rating);

    @Query(value = "SELECT COUNT(R.no) FROM (SELECT no,theme_no,success FROM recode WHERE member_no = :memberNo AND success = true GROUP BY theme_no) AS R", nativeQuery = true)
    int countTotalSuccessNum(Long memberNo);

    @Query(value = "SELECT COUNT(R.no) FROM (SELECT no,theme_no FROM recode WHERE member_no = :memberNo GROUP BY theme_no ) AS R", nativeQuery = true)
    int countTotalVisitNum(Long memberNo);

    @Query(value = "SELECT COUNT(R.no) FROM (SELECT r.no,r.success,t.area_type FROM recode r, theme t WHERE member_no = :memberNo AND success = true AND r.theme_no = t.no GROUP BY theme_no) AS R WHERE R.area_type=:areaName", nativeQuery = true)
    int countSuccessNum(Long memberNo, String areaName);

    @Query(value = "SELECT COUNT(R.no) FROM (SELECT r.no,t.area_type FROM recode r, theme t WHERE member_no = :memberNo AND r.theme_no = t.no GROUP BY theme_no) AS R WHERE R.area_type=:areaName", nativeQuery = true)
    int countVisitNum(Long memberNo,String areaName);

    @Query(value = "SELECT COUNT(r.member_no) FROM Recode r WHERE r.member_no = :memberNo", nativeQuery = true)
    int countRecode(@Param("memberNo")Long no);

    @Query(value = "SELECT COUNT(T.no) FROM (SELECT no,theme_no,success FROM recode WHERE member_no = :memberNo AND success = true) AS T", nativeQuery = true)
    int countSuccessRecode(@Param("memberNo")Long no);

}