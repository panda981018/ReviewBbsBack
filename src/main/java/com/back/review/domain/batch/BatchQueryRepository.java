package com.back.review.domain.batch;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BatchQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Tuple> findByYearAndMonth(String categoryName, int year, int month) {
        return jpaQueryFactory.select(QBatchResult.batchResult.staticsDate, QBatchResult.batchResult.bbsCount)
                .from(QBatchResult.batchResult)
                .where(QBatchResult.batchResult.staticsDate.year().eq(year)
                        .and(QBatchResult.batchResult.staticsDate.month().eq(month))
                        .and(QBatchResult.batchResult.name.eq(categoryName.toUpperCase())))
                .orderBy(QBatchResult.batchResult.staticsDate.asc())
                .fetchResults()
                .getResults();
    }
}