package com.reportportal.models.launch;

import lombok.Data;

@Data
public class Page
{
    private Integer number;
    private Integer size;
    private Integer totalElements;
    private Integer totalPages;
}
