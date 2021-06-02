package top.joeallen.closedbook;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.net.InetAddress;

/**
 * @author JoeAllen_Li
 */
@Slf4j
@EnableOpenApi
@MapperScan(basePackages = {"top.joeallen.closedbook.dao"})
@SpringBootApplication
public class CopyTeachApplication implements ApplicationListener<WebServerInitializedEvent> {

    public static void main(String[] args) {
        SpringApplication.run(CopyTeachApplication.class, args);
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        WebServer server = event.getWebServer();
        WebServerApplicationContext context = event.getApplicationContext();
        Environment env = context.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        int port = server.getPort();
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        log.info(
                "\n"
                        + "---------------------------------------------------------\n"
                        + "\t\t\t\t应用程序正在运行!访问地址:\n"
                        + "\t\t\t本地:\thttp://localhost:{}\n"
                        + "\t\t\t外部:\thttp://{}:{}{}\n"
                        + "---------------------------------------------------------\n",
                port,
                ip,
                port,
                contextPath);
    }
}
