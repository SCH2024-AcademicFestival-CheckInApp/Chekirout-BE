package com.sch.chekirout.program.application;

import com.sch.chekirout.admin.application.dto.request.CategoryRequest;
import com.sch.chekirout.admin.application.dto.response.CategoryResponse;
import com.sch.chekirout.program.domain.Category;
import com.sch.chekirout.program.domain.repository.CategoryRepository;
import com.sch.chekirout.program.exception.CategoryDuplicatedException;
import com.sch.chekirout.program.exception.CategoryNotFoundException;
import com.sch.chekirout.program.domain.Program;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Long saveCategory(CategoryRequest request) {
        checkDuplicatedCategoryName(request.getName());
        return categoryRepository.save(request.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAllByDeletedAtIsNull().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Long> getValidCategoryIds() {
        return categoryRepository.findAllByDeletedAtIsNull().stream()
                .map(Category::getId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long id) {
        return categoryRepository.findByIdAndDeletedAtIsNull(id)
                .map(CategoryResponse::from)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryByName(String name) {
        return categoryRepository.findByNameAndDeletedAtIsNull(name)
                .map(CategoryResponse::from)
                .orElseThrow(() -> new CategoryNotFoundException(name));
    }

    @Transactional
    public void updateCategory(Long categoryId, CategoryRequest request) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        if(!category.getName().equals(request.getName())) {
            checkDuplicatedCategoryName(request.getName());
        }

        category.update(request);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        category.getPrograms().forEach(Program::delete);
        category.delete();
    }

    public void checkDuplicatedCategoryName(String name) {
        if (categoryRepository.existsByNameAndDeletedAtIsNull(name)) {
            throw new CategoryDuplicatedException(name);
        }
    }
}
