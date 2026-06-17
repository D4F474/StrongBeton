package com.strongBeton.strongBeton.dto;

public class AdminFeedStatsDTO {
    private long totalPosts;
    private long visiblePosts;
    private long hiddenPosts;
    private long pinnedPosts;
    private long reportedPosts;
    private long totalComments;

    public long getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(long totalPosts) {
        this.totalPosts = totalPosts;
    }

    public long getVisiblePosts() {
        return visiblePosts;
    }

    public void setVisiblePosts(long visiblePosts) {
        this.visiblePosts = visiblePosts;
    }

    public long getHiddenPosts() {
        return hiddenPosts;
    }

    public void setHiddenPosts(long hiddenPosts) {
        this.hiddenPosts = hiddenPosts;
    }

    public long getPinnedPosts() {
        return pinnedPosts;
    }

    public void setPinnedPosts(long pinnedPosts) {
        this.pinnedPosts = pinnedPosts;
    }

    public long getReportedPosts() {
        return reportedPosts;
    }

    public void setReportedPosts(long reportedPosts) {
        this.reportedPosts = reportedPosts;
    }

    public long getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(long totalComments) {
        this.totalComments = totalComments;
    }
}
