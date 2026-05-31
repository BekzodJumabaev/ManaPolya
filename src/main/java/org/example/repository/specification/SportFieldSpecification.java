package org.example.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.dto.SportFileldSearchDto;
import org.example.entity.SportField;
import org.example.enums.FieldStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class SportFieldSpecification {

    public static Specification<SportField> buildSpecification(SportFileldSearchDto searchDto) {
        return  (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("status"), FieldStatus.APPROVED));
            if (searchDto == null) {
                return criteriaBuilder.equal(root.get("deleted"), false);
            }

            if (searchDto.getSearch() != null && !searchDto.getSearch().trim().isEmpty()){
                String keyword = "%" + searchDto.getSearch().trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), keyword));
            }

            if (searchDto.getDistrictId() != null){
                predicates.add(criteriaBuilder.equal(root.get("district").get("id"), searchDto.getDistrictId()));
            }

            if (searchDto.getRegionId() != null){
                predicates.add(criteriaBuilder.equal(root.get("district").get("region").get("id"), searchDto.getRegionId()));
            }

            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }




}
