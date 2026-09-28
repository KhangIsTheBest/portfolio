-- V8__create_leetcode_submissions_table.sql
-- Create table for storing LeetCode accepted submissions and solution source code

CREATE TABLE IF NOT EXISTS leetcode_submissions (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    title_slug VARCHAR(255) NOT NULL,
    difficulty VARCHAR(20),
    lang VARCHAR(50),
    status_display VARCHAR(50) DEFAULT 'Accepted',
    runtime VARCHAR(50),
    memory VARCHAR(50),
    code TEXT,
    timestamp BIGINT,
    submitted_at TIMESTAMP WITHOUT TIME ZONE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_leetcode_submissions_difficulty ON leetcode_submissions(difficulty);
CREATE INDEX IF NOT EXISTS idx_leetcode_submissions_lang ON leetcode_submissions(lang);
CREATE INDEX IF NOT EXISTS idx_leetcode_submissions_time ON leetcode_submissions(timestamp DESC);
