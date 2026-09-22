-- V4__add_cv_urls_to_profile.sql
-- Add Vietnamese and English CV URLs to profiles table

ALTER TABLE profiles
ADD COLUMN IF NOT EXISTS cv_vi_url VARCHAR(500),
ADD COLUMN IF NOT EXISTS cv_en_url VARCHAR(500);
