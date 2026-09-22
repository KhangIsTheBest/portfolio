-- V6__create_youtube_tables.sql
-- Create YouTube Videos table

CREATE TABLE IF NOT EXISTS youtube_videos (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    video_id VARCHAR(50) NOT NULL,
    description TEXT,
    category VARCHAR(100) DEFAULT 'General',
    thumbnail_url VARCHAR(500),
    duration VARCHAR(20),
    published_at TIMESTAMP WITHOUT TIME ZONE,
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Index for category filtering and display order
CREATE INDEX IF NOT EXISTS idx_youtube_videos_category ON youtube_videos(category);
CREATE INDEX IF NOT EXISTS idx_youtube_videos_order ON youtube_videos(display_order ASC, created_at DESC);
