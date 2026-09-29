package org.example.c.backend.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页插件，DbType.MYSQL 自动生成 limit
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(500L); // 限制单页最多500条，防止一次性查海量数据
        paginationInnerInterceptor.setOverflow(false); // 页码超过最大页：false 返回空；true 返回最后一页

        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        // 可以在这里继续添加其他插件（比如防全表更新删除插件）
        return interceptor;
    }
}
