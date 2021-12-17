package com.back.review.domain.favorite;

import com.back.review.domain.map.MapEntity;
import com.back.review.domain.member.MemberEntity;
import com.back.review.dto.FavoriteDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "JW_FAVORITE")
public class FavoriteEntity {

    @Id
    @SequenceGenerator(
            name = "FAVORITE_SEQ_GEN",
            sequenceName = "JW_FAVORITE_SEQ",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "FAVORITE_SEQ_GEN")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mapId")
    private MapEntity map;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private MemberEntity member;

    public void setMap(MapEntity map) {
        this.map = map;
    }

    public void setMember(MemberEntity member) {
        this.member = member;
    }

    public FavoriteDto toDto() {
        return FavoriteDto.builder()
                .id(id)
                .latitude(map.getLatitude())
                .longitude(map.getLongitude())
                .placeName(map.getPlaceName())
                .mapId(map.getId())
                .build();
    }

    @Builder
    public FavoriteEntity(Long id) {
        this.id = id;
    }
}
