package com.devteria.post.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
    int currentPage; // Đại diện cho số trang hiện tại
    int totalPages; // Tổng số trang có sẵn dựa trên kích thước trang và tổng số phần tử (totalElements).
    int pageSize; // Số phần tử (items) trong mỗi trang
    long totalElements; //  Tổng số phần tử trong toàn bộ tập dữ liệu

    @Builder.Default
    private List<T> data = Collections.emptyList();
}
