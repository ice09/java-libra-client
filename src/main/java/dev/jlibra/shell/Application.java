package dev.jlibra.shell;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.shell.jline.PromptProvider;

import java.security.Security;

@SpringBootApplication
@ComponentScan(basePackages = {"dev.jlibra"})
public class Application {
    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PromptProvider libraPromptProvider() {
        return () -> new AttributedString("jlibra:>",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
    }


}
