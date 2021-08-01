package az.code.telegram_bot_api.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BaseUtilsTest {

    @Test
    @DisplayName("BaseUtils - pagination Result if pages empty Valid")
    void paginationResult_test1() {
        Page<String> page = new PageImpl<>(Collections.emptyList());
        assertEquals(new ArrayList<String>(), BaseUtils.paginationResult(page));
    }

    @Test
    @DisplayName("BaseUtils - pagination Result if pages have content Valid")
    void paginationResult_test2() {
        Page<String> page = new PageImpl<>(Collections.singletonList("test"));
        assertEquals(Collections.singletonList("test"), BaseUtils.paginationResult(page));
    }

    @Test
    @DisplayName("BaseUtils - get Pageable Valid")
    void getPageable() {
        assertEquals(PageRequest.of(5, 10, Sort.by("sort")), BaseUtils.getPageable(5, 10, "sort"));
    }

}