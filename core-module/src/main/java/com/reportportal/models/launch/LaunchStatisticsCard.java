package com.reportportal.models.launch;

import lombok.Data;

@Data
public class LaunchStatisticsCard
{
    private Integer index;
    private Integer total;
    private Integer passed;
    private Integer failed;
    private Integer skipped;
    private Integer productBug;
    private Integer autoBug;
    private Integer systemIssue;
    private Integer toInvestigate;

    public LaunchStatisticsCard(Integer index, Integer total, Integer passed, Integer failed, Integer skipped,
            Integer productBug, Integer autoBug, Integer systemIssue, Integer toInvestigate)
    {
        this.index = index;
        this.total = total;
        this.passed = passed;
        this.failed = failed;
        this.skipped = skipped;
        this.productBug = productBug;
        this.autoBug = autoBug;
        this.systemIssue = systemIssue;
        this.toInvestigate = toInvestigate;
    }
}

