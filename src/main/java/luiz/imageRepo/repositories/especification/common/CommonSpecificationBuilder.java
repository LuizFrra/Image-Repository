package luiz.imageRepo.repositories.especification.common;

import luiz.imageRepo.utils.Constants;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommonSpecificationBuilder<S> {

    private final List<SearchCriteria> params;

    public CommonSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public void with(String search) {
        if(search == null) return;

        String []filters = search.split(",");

        Arrays.stream(filters).forEach((filter) -> {
            String []fields = filter.split(Constants.FILTER_OPERATOR);
            Matcher matcher = Pattern.compile(Constants.FILTER_OPERATOR).matcher(filter);
            if(fields.length == 2 && matcher.find())
                params.add(new SearchCriteria(fields[0], matcher.group(), fields[1]));
        });

    }

    public Specification<S> build() {
        if(params.size() == 0) return null;

        List<Specification> specs = params.stream().map(CommonSpecification::new).collect(Collectors.toList());

        Specification<S> result = specs.get(0);

        for(int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }

        return result;
    }
}

