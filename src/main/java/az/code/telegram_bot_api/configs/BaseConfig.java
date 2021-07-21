package az.code.telegram_bot_api.configs;

import az.code.telegram_bot_api.configs.security.TokenInterceptor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableTransactionManagement
public class BaseConfig implements WebMvcConfigurer {
    final
    TokenInterceptor tokenInterceptor;

    public BaseConfig(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
