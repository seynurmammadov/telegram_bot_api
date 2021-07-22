package az.code.telegram_bot_api.utils;

import org.springframework.data.domain.Page;

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
}
