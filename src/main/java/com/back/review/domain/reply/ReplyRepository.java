package com.back.review.domain.reply;

import com.back.review.domain.bbs.BbsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {

    Optional<ReplyEntity> findById(Long id);
    List<ReplyEntity> findByBbs(BbsEntity bbs);
}
