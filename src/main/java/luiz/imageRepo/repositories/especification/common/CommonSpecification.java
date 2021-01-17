package luiz.imageRepo.repositories.especification.common;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class CommonSpecification<S> implements Specification<S> {

    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<S> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return criteriaBuilder.greaterThan(root.get(criteria.getKey()),
                    criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(">:")) {
            return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()),
                    criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return criteriaBuilder.lessThan(root.get(criteria.getKey()),
                    criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":=")) {
            return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()),
                    criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if(criteria.getValue().equals("false")) {
                return criteriaBuilder.equal(root.get(criteria.getKey()), false);
            } else if(criteria.getValue().equals("true")) {
                return criteriaBuilder.equal(root.get(criteria.getKey()), true);
            } else if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }

        return null;
    }
}

