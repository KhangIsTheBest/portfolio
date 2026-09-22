-- V5__add_project_dates_and_markdown.sql
-- Add project timeline, content type, and external profile fields

ALTER TABLE projects
ADD COLUMN IF NOT EXISTS start_date VARCHAR(20),
ADD COLUMN IF NOT EXISTS end_date VARCHAR(20),
ADD COLUMN IF NOT EXISTS is_current BOOLEAN DEFAULT FALSE,
ADD COLUMN IF NOT EXISTS content_type VARCHAR(20) DEFAULT 'MARKDOWN';

ALTER TABLE profiles
ADD COLUMN IF NOT EXISTS leetcode_username VARCHAR(100),
ADD COLUMN IF NOT EXISTS leetcode_session TEXT,
ADD COLUMN IF NOT EXISTS youtube_channel_id VARCHAR(100),
ADD COLUMN IF NOT EXISTS youtube_handle VARCHAR(100);
