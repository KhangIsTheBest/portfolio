-- V2__seed_initial_data.sql
-- Insert default Profile if not exists
INSERT INTO profiles (id, full_name, title, about_me, github_url, linkedin_url, email, avatar_url, updated_at)
VALUES (
    1,
    'Phan Duy Khang',
    'Backend / Software Engineer',
    'Software Engineer with strong foundation in Java, Spring Boot, Microservices, and Cloud Infrastructure. Passionate about building high-performance, resilient, and scalable backend distributed systems.',
    'https://github.com/KhangIsTheBest',
    'https://linkedin.com/in/phanduykhang',
    'pdkhang1304@gmail.com',
    'https://api.dicebear.com/7.x/bottts/svg?seed=PhanDuyKhang',
    CURRENT_TIMESTAMP
)
ON CONFLICT (id) DO NOTHING;

-- Insert default Technologies
INSERT INTO technologies (name, icon_url, created_at)
VALUES
    ('Java 21', 'https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg', CURRENT_TIMESTAMP),
    ('Spring Boot 3', 'https://raw.githubusercontent.com/devicons/devicon/master/icons/spring/spring-original.svg', CURRENT_TIMESTAMP),
    ('PostgreSQL', 'https://raw.githubusercontent.com/devicons/devicon/master/icons/postgresql/postgresql-original.svg', CURRENT_TIMESTAMP),
    ('Redis', 'https://raw.githubusercontent.com/devicons/devicon/master/icons/redis/redis-original.svg', CURRENT_TIMESTAMP),
    ('Docker', 'https://raw.githubusercontent.com/devicons/devicon/master/icons/docker/docker-original.svg', CURRENT_TIMESTAMP),
    ('Next.js', 'https://raw.githubusercontent.com/devicons/devicon/master/icons/nextjs/nextjs-original.svg', CURRENT_TIMESTAMP),
    ('TypeScript', 'https://raw.githubusercontent.com/devicons/devicon/master/icons/typescript/typescript-original.svg', CURRENT_TIMESTAMP),
    ('MinIO / S3', 'https://min.io/resources/img/logo/MINIO_wordmark.svg', CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;
