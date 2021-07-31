package az.code.telegram_bot_api.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class BaseUtils {

    public static <T> List<T> paginationResult(Page<T> pageResult) {
        if (pageResult.hasContent()) {
            return pageResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    public static PageRequest getPageable(Integer pageNo, Integer itemsCount, String sortBy) {
        return PageRequest.of(pageNo, itemsCount, Sort.by(sortBy));
    }

}
