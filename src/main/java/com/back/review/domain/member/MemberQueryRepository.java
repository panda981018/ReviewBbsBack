package com.back.review.domain.member;

import com.back.review.enumclass.Role;
import com.back.review.util.QueryDslUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;


@RequiredArgsConstructor
@Repository
public class MemberQueryRepository { // CRUD

    private final JPAQueryFactory jpaQueryFactory;

    public QueryResults<String> findByNickname(Long id) throws SQLException {

        QueryResults<String> rtResult = jpaQueryFactory.select(QMemberEntity.memberEntity.nickname)
                .from(QMemberEntity.memberEntity)
                .where(QMemberEntity.memberEntity.id.ne(id))
                .fetchResults();

        return rtResult;
    }

    public Page<MemberEntity> findAllExceptAdmin(Pageable pageable) {
        List<OrderSpecifier> order = QueryDslUtil.getAllOrderSpecifiers(pageable);

        QueryResults<MemberEntity> rt = jpaQueryFactory.selectFrom(QMemberEntity.memberEntity)
                .where(QMemberEntity.memberEntity.role.ne(Role.ADMIN))
                .orderBy(order.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(rt.getResults(), pageable, rt.getTotal());
    }

    // delete
    @Transactional
    public void deleteUser(String username) throws SQLException {

    }

}
