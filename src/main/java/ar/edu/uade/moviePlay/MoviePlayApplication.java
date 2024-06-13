package ar.edu.uade.moviePlay;

import ar.edu.uade.moviePlay.util.ScopeUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class MoviePlayApplication {

	public static void main(String[] args) {
		ScopeUtils.calculateScopeSuffix();
		new SpringApplicationBuilder(MoviePlayApplication.class).registerShutdownHook(true).run(args);
	}

}
