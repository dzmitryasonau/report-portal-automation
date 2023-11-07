package com.reportportal.models.launch;

import lombok.Data;
import java.util.List;

@Data
public class Launch {
    private List<Content> content;
    private Page page;
}

