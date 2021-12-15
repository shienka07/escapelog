package com.recoders.escapelog.repository;

import com.recoders.escapelog.domain.Recode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryRepository extends JpaRepository<Recode, Long> {

    List<Recode> findAll();

    List<Recode> findByMember_NicknameOrderByRegdateDesc(String nickname);

}