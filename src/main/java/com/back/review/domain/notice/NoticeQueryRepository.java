package com.back.review.domain.notice;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Repository
public class NoticeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public void updateViews(Long noticeId, int view) {
        jpaQueryFactory.update(QNoticeEntity.noticeEntity)
                .set(QNoticeEntity.noticeEntity.views, view+1)
                .where(QNoticeEntity.noticeEntity.id.eq(noticeId))
                .execute();
    }
}