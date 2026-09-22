-- V3__seed_demo_projects.sql
-- Seed high-impact Software Engineer projects

INSERT INTO projects (
    id, title, slug, short_description, content, github_url, demo_url, thumbnail_url, featured, status, created_at, updated_at
)
VALUES (
    1,
    'Enterprise Portfolio Platform',
    'portfolio-platform',
    'A production-grade, decoupled software engineer portfolio platform engineered with Java 21, Spring Boot 3, Redis Cache, PostgreSQL, and Next.js 16 App Router.',
    '## System Architecture Overview\n\nThis project represents a decoupled, production-ready developer portfolio and CMS engine built with modern backend engineering principles.\n\n### Core Engineering Highlights\n- **Micro-Layered Architecture**: Decoupled Next.js 16 SSR frontend communicating via RESTful APIs with Spring Boot 3 backend.\n- **Flyway Database Versioning**: Automated schema migration and reliable version control for PostgreSQL.\n- **Redis In-Memory Caching & Distributed Rate Limiting**: Low-latency read-through caching and token-bucket protection against DDoS.\n- **Testcontainers Testing**: Automated integration test pipeline testing against real containerized PostgreSQL and Redis instances.\n- **Dual-Auth Security**: Google OAuth2 ID Token Verification and stateless JWT RBAC security.\n\n### Tech Stack\n- Backend: Java 21, Spring Boot 3, Spring Security 6, JPA / Hibernate, Flyway, Redis, MinIO S3\n- Frontend: Next.js 16, React 19, TypeScript, Tailwind CSS 4\n- DevOps: Docker Compose, GitHub Actions CI/CD',
    'https://github.com/KhangIsTheBest/Portfolio',
    'https://khang.kamy.space',
    'https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=1200&auto=format&fit=crop',
    TRUE,
    'PUBLISHED',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    2,
    'Distributed Booking & Reservation System',
    'distributed-booking-system',
    'High-concurrency booking engine handling distributed locking, race conditions, and real-time seat reservation with Redis Redlock & PostgreSQL.',
    '## Engineering Challenges & Solutions\n\n### High Concurrency & Distributed Locking\nSolved race conditions in ticket/seat reservation by implementing **Redis Redlock distributed locking mechanism**, ensuring zero double-booking even under massive concurrent surges.\n\n### Key Features\n- Event-driven asynchronous notifications via RabbitMQ message broker.\n- Optimistic locking with version fields in PostgreSQL.\n- Comprehensive Prometheus metrics & Grafana dashboard monitoring.\n\n### Tech Stack\n- Java 21, Spring Boot 3, PostgreSQL, Redis Redlock, RabbitMQ, Docker',
    'https://github.com/KhangIsTheBest',
    'https://khang.kamy.space',
    'https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=1200&auto=format&fit=crop',
    TRUE,
    'PUBLISHED',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    3,
    'Microservices Cloud E-Commerce API',
    'microservices-ecommerce-api',
    'Scalable cloud-native e-commerce backend built with Spring Cloud, Eureka Service Discovery, Spring Cloud Gateway, and Resilience4j Circuit Breaker.',
    '## System Design & Microservices Architecture\n\n### Architectural Highlights\n- **API Gateway & Routing**: Spring Cloud Gateway handling centralized JWT authentication, CORS, and request rate-limiting.\n- **Fault Tolerance**: Resilience4j Circuit Breakers, Bulkhead, and Retry patterns for high availability.\n- **Data Management**: Database-per-service pattern with Saga pattern for distributed transactions.\n\n### Tech Stack\n- Spring Boot 3, Spring Cloud Gateway, Eureka, Resilience4j, Kafka, PostgreSQL',
    'https://github.com/KhangIsTheBest',
    'https://khang.kamy.space',
    'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=1200&auto=format&fit=crop',
    FALSE,
    'PUBLISHED',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (id) DO NOTHING;

-- Map projects to default technologies
INSERT INTO project_technologies (project_id, technology_id)
SELECT 1, id FROM technologies WHERE name IN ('Java 21', 'Spring Boot 3', 'PostgreSQL', 'Redis', 'Docker', 'Next.js', 'TypeScript')
ON CONFLICT DO NOTHING;

INSERT INTO project_technologies (project_id, technology_id)
SELECT 2, id FROM technologies WHERE name IN ('Java 21', 'Spring Boot 3', 'PostgreSQL', 'Redis', 'Docker')
ON CONFLICT DO NOTHING;

INSERT INTO project_technologies (project_id, technology_id)
SELECT 3, id FROM technologies WHERE name IN ('Java 21', 'Spring Boot 3', 'PostgreSQL', 'Docker')
ON CONFLICT DO NOTHING;
