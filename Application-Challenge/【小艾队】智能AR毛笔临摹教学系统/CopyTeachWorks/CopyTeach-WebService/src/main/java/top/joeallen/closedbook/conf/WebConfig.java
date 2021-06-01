package top.joeallen.closedbook.conf;

import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author qt
 * @Date 2020/9/10
 * @Description web配置
 */
@Configuration //加配置注解可以扫描到
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //System.out.println("---进入设置项目启动页面---");
        //"/"这里是访问路径，"login"是页面名称 ,login页面在 templates 文件夹下
        //registry.addViewController( "/" ).setViewName("forward:/login.html");
//        registry.addViewController("/").setViewName("login");
        //设置优先级
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        WebMvcConfigurer.super.addViewControllers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要拦截的路径
        String[] addPathPatterns = {
                "/**"//所有路径都被拦截
        };
        //不需要拦截的路径
        String[] excludePathPatterns = {
                /*"/**"//所有路径*/
        };
        //注册登陆拦截器
//        registry.addInterceptor(new LoginInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
    }

    //跨域请求配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")// 对接口配置跨域设置
                .allowedHeaders("*")// 允许任何头
                .allowedMethods("POST", "GET")// 允许方法（post、get等）
                .allowedOrigins("*")// 允许任何域名使用
                .allowCredentials(true);
    }

    //配置类中声明错误页面Bean
    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return new ErrorPageConfig();
    }
}
