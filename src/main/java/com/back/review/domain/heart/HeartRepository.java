package com.back.review.domain.heart;

import com.back.review.domain.bbs.BbsEntity;
import com.back.review.domain.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<HeartEntity, Long> {

    Optional<HeartEntity> findById(Long id);
    Optional<HeartEntity> findByBbsAndMember(BbsEntity bbs, MemberEntity member);
    List<HeartEntity> findByBbs(BbsEntity bbs);
    boolean existsByBbsAndMember(BbsEntity bbs, MemberEntity member);
}
