package com.recoders.escapelog.repository;

import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.service.MemberService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByLibraryName(String libraryName);

}
