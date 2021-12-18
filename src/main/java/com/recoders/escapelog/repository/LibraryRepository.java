package com.recoders.escapelog.repository;

import com.recoders.escapelog.domain.Recode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryRepository extends JpaRepository<Recode, Long> {

    List<Recode> findByMember_NicknameOrderByRegdateDesc(String nickname);

    Optional<Recode> deleteByNo(Long no);

}