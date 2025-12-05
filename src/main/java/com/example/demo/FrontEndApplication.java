package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.example.demo.app.domain.model.UserRole;
import com.example.demo.app.infrastructure.adapters.output.persistence.entity.UserRolesEntity;
import com.example.demo.app.infrastructure.adapters.output.persistence.mapper.UserRoleMapper;

@SpringBootApplication
public class FrontEndApplication extends SpringBootServletInitializer {
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(FrontEndApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(FrontEndApplication.class, args);
    }
    
    
    @Bean
    UserRoleMapper userRoleMapper(){
   	return new UserRoleMapper() {
   		
   		@Override
   		public UserRolesEntity toEntity(UserRole domain) {
   			return new UserRolesEntity();
   		}
   		
   		@Override
   		public UserRole toDomain(UserRolesEntity entity) {
   			return new UserRole();
   		}
   	};
   }
    
}



