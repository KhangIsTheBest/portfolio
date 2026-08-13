package com.khangdt.portfolio.project.init;

import com.khangdt.portfolio.auth.entity.User;
import com.khangdt.portfolio.auth.repository.UserRepository;
import com.khangdt.portfolio.blog.entity.Blog;
import com.khangdt.portfolio.blog.repository.BlogRepository;
import com.khangdt.portfolio.project.entity.Project;
import com.khangdt.portfolio.project.entity.ProjectStatus;
import com.khangdt.portfolio.project.repository.ProjectRepository;
import com.khangdt.portfolio.technology.entity.Technology;
import com.khangdt.portfolio.technology.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectDataInitializer implements CommandLineRunner {

    private final TechnologyRepository technologyRepository;
    private final ProjectRepository projectRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        seedTechnologies();
        seedProjects();
        seedBlogs();
    }

    private void seedTechnologies() {
        if (technologyRepository.count() > 0) return;

        List<Technology> techs = Arrays.asList(
                Technology.builder().name("Java").iconUrl("Java").build(),
                Technology.builder().name("Spring Boot").iconUrl("Spring").build(),
                Technology.builder().name("TypeScript").iconUrl("TypeScript").build(),
                Technology.builder().name("React").iconUrl("React").build(),
                Technology.builder().name("Next.js").iconUrl("NextJS").build(),
                Technology.builder().name("Angular").iconUrl("Angular").build(),
                Technology.builder().name("Node.js").iconUrl("NodeJS").build(),
                Technology.builder().name("Express.js").iconUrl("Express").build(),
                Technology.builder().name("C# / .NET").iconUrl("CSharp").build(),
                Technology.builder().name("PostgreSQL").iconUrl("PostgreSQL").build(),
                Technology.builder().name("MySQL").iconUrl("MySQL").build(),
                Technology.builder().name("Docker").iconUrl("Docker").build(),
                Technology.builder().name("Git").iconUrl("Git").build(),
                Technology.builder().name("RESTful API").iconUrl("API").build()
        );

        technologyRepository.saveAll(techs);
        log.info("Seeded {} technologies into database.", techs.size());
    }

    private void seedProjects() {
        if (projectRepository.count() > 0) return;

        User defaultUser = userRepository.findAll().stream().findFirst().orElse(null);

        Map<String, Technology> techMap = new HashMap<>();
        technologyRepository.findAll().forEach(t -> techMap.put(t.getName(), t));

        Set<Technology> p1Techs = new HashSet<>();
        addTechIfPresent(p1Techs, techMap, "TypeScript", "Next.js", "Spring Boot", "PostgreSQL", "Docker");

        Set<Technology> p2Techs = new HashSet<>();
        addTechIfPresent(p2Techs, techMap, "Angular", "Node.js", "Express.js", "MySQL", "Docker");

        Set<Technology> p3Techs = new HashSet<>();
        addTechIfPresent(p3Techs, techMap, "C# / .NET", "MySQL", "Git");

        Project p1 = Project.builder()
                .title("Dự án Portfolio cá nhân")
                .slug("portfolio")
                .shortDescription("Hệ thống Portfolio cá nhân tương tác cao tích hợp giao diện GUI Glassmorphism hiện đại và chế độ Terminal CLI, kết nối Spring Boot Backend.")
                .content("Xây dựng hệ thống Portfolio cá nhân tương tác cao, hỗ trợ chuyển đổi linh hoạt giữa giao diện Modern GUI Glassmorphism và giao diện CLI. Thiết kế cơ sở dữ liệu quan hệ PostgreSQL, đồng bộ hóa thực thể qua Spring Data JPA/Hibernate. Phát triển hệ thống xác thực người dùng và phân quyền truy cập sử dụng Spring Security & JWT Token. Tích hợp MinIO / Cloudinary Object Storage cho lưu trữ hình ảnh persistent.")
                .githubUrl("https://github.com/KhangIsTheBest/portfolio-cli")
                .demoUrl("https://portfolio.vercel.app")
                .thumbnailUrl("https://images.unsplash.com/photo-1542831371-29b0f74f9713?w=800&auto=format&fit=crop&q=60")
                .featured(true)
                .status(ProjectStatus.PUBLISHED)
                .createdBy(defaultUser)
                .technologies(p1Techs)
                .build();

        Project p2 = Project.builder()
                .title("LFYS – Nền tảng học lập trình trực tuyến (Khóa luận tốt nghiệp)")
                .slug("lfys-coding-platform")
                .shortDescription("Nền tảng học lập trình trực tuyến tích hợp khóa học, tài liệu, online IDE chấm code trực tiếp và hệ thống contest.")
                .content("Xây dựng nền tảng học lập trình trực tuyến tích hợp khóa học, tài liệu, online IDE và hệ thống contest. Phát triển RESTful APIs bằng ExpressJS cùng JWT Authentication và Role-based Authorization. Thiết kế hệ thống cơ sở dữ liệu với khoảng 60 bảng dữ liệu và tối ưu truy vấn bằng indexing. Tích hợp Judge0 để chấm code trực tiếp. Docker hóa frontend, backend, MySQL. Sử dụng GitHub Actions để tự động build và deploy.")
                .githubUrl("https://github.com/DinosDATN/KL")
                .demoUrl("https://lfys.example.com")
                .thumbnailUrl("https://images.unsplash.com/photo-1557821552-17105176677c?w=800&auto=format&fit=crop&q=60")
                .featured(true)
                .status(ProjectStatus.PUBLISHED)
                .createdBy(defaultUser)
                .technologies(p2Techs)
                .build();

        Project p3 = Project.builder()
                .title("Hệ thống Quản lý & Lập lịch Tác vụ Ngầm (.NET C#)")
                .slug("net-task-scheduler")
                .shortDescription("Hệ thống xử lý tác vụ ngầm hiệu năng cao và lập lịch công việc định kỳ sử dụng C# .NET, Hangfire và TickerQ.")
                .content("Hệ thống được phát triển trong thời gian thực tập tại Tập Đoàn Đầu Tư Công Nghệ Nam Long. Xử lý các tác vụ tốn thời gian ra khỏi luồng xử lý HTTP chính. Sử dụng Hangfire để quản lý lập lịch tác vụ đáng tin cậy với khả năng tự thử lại khi lỗi (automatic retry). Kết hợp thư viện TickerQ để tối ưu hàng đợi tin nhắn và phân luồng tác vụ.")
                .githubUrl("https://github.com/KhangIsTheBest/net-job-scheduler")
                .thumbnailUrl("https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?w=800&auto=format&fit=crop&q=60")
                .featured(false)
                .status(ProjectStatus.PUBLISHED)
                .createdBy(defaultUser)
                .technologies(p3Techs)
                .build();

        projectRepository.saveAll(Arrays.asList(p1, p2, p3));
        log.info("Seeded 3 sample projects into database.");
    }

    private void seedBlogs() {
        if (blogRepository.count() > 0) return;

        User defaultUser = userRepository.findAll().stream().findFirst().orElse(null);

        Blog b1 = Blog.builder()
                .title("Getting Started with Next.js 16 & Tailwind CSS v4")
                .slug("nextjs16-tailwind-v4")
                .summary("A comprehensive developer guide on setting up a new Next.js 16 project utilizing the freshly updated Tailwind CSS v4 compiler.")
                .content("Next.js 16 brings powerful updates to the App Router and async requests, while Tailwind CSS v4 introduces a brand-new native CSS-first engine. Together, they create a blazing fast developer experience. In this post, we look at configuring custom themes directly within `@theme` syntax in `globals.css` and how to make the most of server-side data fetching.")
                .thumbnailUrl("https://images.unsplash.com/photo-1499750310107-5fef28a66643?w=800&auto=format&fit=crop&q=60")
                .published(true)
                .author(defaultUser)
                .build();

        Blog b2 = Blog.builder()
                .title("Building Resilient REST APIs with Spring Boot 3.5")
                .slug("spring-boot-3-resilience")
                .summary("Deep dive into configuring global exception handlers, custom response envelopes, validation, and CORS policies in Java backends.")
                .content("Writing code that works under perfect conditions is easy. Writing code that handles database outages, validation failures, and unauthorized request attempts gracefully is what distinguishes senior engineers. In this blog post, we dissect how to build a unified response wrapper, handle bad input requests using `@Valid`, and set up secure CORS configurations for modern SPAs.")
                .thumbnailUrl("https://images.unsplash.com/photo-1461749280684-dccba630e2f6?w=800&auto=format&fit=crop&q=60")
                .published(true)
                .author(defaultUser)
                .build();

        blogRepository.saveAll(Arrays.asList(b1, b2));
        log.info("Seeded 2 sample blog posts into database.");
    }

    private void addTechIfPresent(Set<Technology> set, Map<String, Technology> map, String... names) {
        for (String name : names) {
            if (map.containsKey(name)) {
                set.add(map.get(name));
            }
        }
    }
}
