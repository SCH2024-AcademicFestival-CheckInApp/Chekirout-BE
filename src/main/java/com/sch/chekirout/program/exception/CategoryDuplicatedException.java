package com.sch.chekirout.program.exception;

import com.sch.chekirout.common.exception.CustomDuplicatedException;

public class CategoryDuplicatedException extends CustomDuplicatedException {

    public CategoryDuplicatedException(final String categoryName) {
        super(String.format("카테고리가 이미 존재합니다. 카테고리 이름 : %s", categoryName));
    }
}
