package com.sch.chekirout.program.exception;

import com.sch.chekirout.common.exception.CustomNotFoundException;

public class CategoryNotFoundException extends CustomNotFoundException {

    public CategoryNotFoundException(final Long categoryId) {
        super(String.format("카테고리를 찾을 수 없습니다. 카테고리 아이디 : %d", categoryId));
    }

    public CategoryNotFoundException(final String categoryName) {
        super(String.format("카테고리를 찾을 수 없습니다. 카테고리 이름 : %d", categoryName));
    }
}
