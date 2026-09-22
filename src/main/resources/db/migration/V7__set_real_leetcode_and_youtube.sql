-- V7__set_real_leetcode_and_youtube.sql
-- Update real credentials for LeetCode profile and YouTube channel

UPDATE profiles 
SET leetcode_username = 'psmNQXkg5O',
    youtube_channel_id = 'https://www.youtube.com/@dkp.13',
    youtube_handle = '@dkp.13'
WHERE id = (SELECT id FROM profiles ORDER BY id ASC LIMIT 1);
