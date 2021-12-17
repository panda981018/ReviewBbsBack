package com.back.review.domain.favorite;

import com.back.review.domain.map.MapEntity;
import com.back.review.domain.member.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    Optional<FavoriteEntity> findById(Long id);
    Optional<FavoriteEntity> findByMemberAndMap(MemberEntity member, MapEntity map);
    Page<FavoriteEntity> findByMember(MemberEntity membere, Pageable pageable);
    boolean existsByMemberAndMap(MemberEntity member, MapEntity map);
}
