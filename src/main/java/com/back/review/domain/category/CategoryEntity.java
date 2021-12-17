package com.back.review.domain.category;

import com.back.review.dto.CategoryDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "JW_CATEGORY")
public class CategoryEntity {

    @Id
    @SequenceGenerator(
            name = "CATEGORY_SEQ_GEN",
            sequenceName = "JW_CATEGORY_SEQ",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "CATEGORY_SEQ_GEN")
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 200)
    private String description;

    public CategoryDto toDto() {
        return CategoryDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    @Builder
    public CategoryEntity(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
