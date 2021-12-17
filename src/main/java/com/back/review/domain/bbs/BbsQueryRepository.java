package com.back.review.domain.bbs;

import com.back.review.domain.category.CategoryEntity;
import com.back.review.util.QueryDslUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Repository
public class BbsQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional
    public void updateBbsViews(Long bbsId, int view) {
        jpaQueryFactory.update(QBbsEntity.bbsEntity)
                .set(QBbsEntity.bbsEntity.bbsViews, view + 1)
                .where(QBbsEntity.bbsEntity.id.eq(bbsId))
                .execute();
    }

    @Transactional
    public void plusLikeCount(Long bbsId, int likeCnt) {
        jpaQueryFactory.update(QBbsEntity.bbsEntity)
                .set(QBbsEntity.bbsEntity.likeCnt, likeCnt + 1)
                .where(QBbsEntity.bbsEntity.id.eq(bbsId))
                .execute();
    }

    @Transactional
    public void minusLikeCount(Long bbsId, int likeCnt) {
        jpaQueryFactory.update(QBbsEntity.bbsEntity)
                .set(QBbsEntity.bbsEntity.likeCnt, likeCnt - 1)
                .where(QBbsEntity.bbsEntity.id.eq(bbsId))
                .execute();
    }

    public Page<BbsEntity> findAllCategoryBbs(CategoryEntity category, Pageable pageable, String property) {

        List<OrderSpecifier> orders = new ArrayList<>();
        Order direction = pageable.getSort().getOrderFor(property)
                .getDirection().isAscending() ? Order.ASC : Order.DESC;
        OrderSpecifier order = QueryDslUtil.getSortedColumn(direction, QBbsEntity.bbsEntity, property);
        orders.add(order);

        QueryResults<BbsEntity> rt = jpaQueryFactory.selectFrom(QBbsEntity.bbsEntity)
                .where(QBbsEntity.bbsEntity.categoryId.eq(category))
                .orderBy(orders.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(rt.getResults(), pageable, rt.getTotal());
    }

    // try 2) SUCCESS
    public List<Tuple> groupByCategory(LocalDateTime from, LocalDateTime to) {
        return jpaQueryFactory.select(
                        QBbsEntity.bbsEntity.categoryId.name,
                        QBbsEntity.bbsEntity.categoryId.name.count()
                )
                .from(QBbsEntity.bbsEntity)
                .where(QBbsEntity.bbsEntity.bbsDate.between(from, to))
                .groupBy(QBbsEntity.bbsEntity.categoryId.name)
                .fetch();
    }
}