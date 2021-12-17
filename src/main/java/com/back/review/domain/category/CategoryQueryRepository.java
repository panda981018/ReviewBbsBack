package com.back.review.domain.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class CategoryQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<String> findAllCategoryName() {
        return jpaQueryFactory.select(QCategoryEntity.categoryEntity.name)
                .from(QCategoryEntity.categoryEntity)
                .orderBy(QCategoryEntity.categoryEntity.id.asc())
                .fetchResults()
                .getResults();
    }
}
