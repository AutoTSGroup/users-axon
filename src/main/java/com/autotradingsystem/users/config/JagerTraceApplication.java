//package com.autotradingsystem.users.config;
//
//import io.micrometer.core.instrument.MeterRegistry;
//import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class JagerTraceApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(JagerTraceApplication.class, args);
//    }
//    @Bean
//    OtlpHttpSpanExporter otlpHttpSpanExporter(@Value("${tracing.url}") String url) {
//        return OtlpHttpSpanExporter.builder()
//                .setEndpoint(url)
//                .build();
//    }
//    @Bean
//    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(@Value("${spring.application.name}") String appName) {
//        return registry -> registry.config().commonTags("application", appName);
//    }
//
//}
