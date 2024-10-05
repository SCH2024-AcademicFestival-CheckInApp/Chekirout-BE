package com.sch.chekirout.admin.presentation;

import com.sch.chekirout.program.application.CategoryService;
import com.sch.chekirout.admin.application.dto.request.CategoryRequest;
import com.sch.chekirout.admin.application.dto.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/categories")
@Tag(name = "Category Admin API", description = "카테고리 관리 API")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 저장", description = "카테고리를 저장하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "카테고리 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "409", description = "중복된 카테고리 이름")
    })
    @PostMapping
    public ResponseEntity<Void> saveCategory(@Valid @RequestBody CategoryRequest request) {
        final Long savedId = categoryService.saveCategory(request);
        final URI location = URI.create("/api/v1/admin/category/" + savedId);
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리 목록을 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        final List<CategoryResponse> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "카테고리 조회(categoryId)", description = "카테고리를 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 없음")
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long categoryId) {
        final CategoryResponse category = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "카테고리 이름으로 조회(name)", description = "카테고리 이름으로 조회하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 없음")
    })
    @GetMapping("/by-name")
    public ResponseEntity<CategoryResponse> getCategoryByName(@RequestParam String name) {
        final CategoryResponse category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(category);
    }


    @Operation(summary = "카테고리 수정", description = "카테고리를 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 수정 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 없음"),
            @ApiResponse(responseCode = "409", description = "중복된 카테고리 이름")
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest request) {
        categoryService.updateCategory(categoryId, request);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "카테고리 없음")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);

        return ResponseEntity.ok().build();
    }
}
