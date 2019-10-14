package com.marcotte.blockhead.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class
 *
 * swaggar is enabled here (does not work at all without the @EnabledSwagger2 option.
 */
@Configuration
// warning removed the @EnableWebMvc because it causes some kind of problem with swagger.
@EnableSwagger2
@ComponentScan("com.marcotte.blockhead")
public class BlockheadConfig //extends WebMvcConfigurerAdapter
{
    @Autowired
    private ApplicationContext context;

    @Value("${cryptocompare.currency.list}")
    private String cryptoCompareCurrencyList;



    /**
     * Setting ignore resource not found, avoiding exceptions to be raised and then the default value
     * in the @Value(${some.property:'defaultValue'}) will work as expected.
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
    {
        PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();
        p.setIgnoreResourceNotFound(true);
        return p;
    }

    @Bean
    public FilterRegistrationBean corsFilter()
    {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    public String getCryptoCompareCurrencyList() {
        return cryptoCompareCurrencyList;
    }
}
