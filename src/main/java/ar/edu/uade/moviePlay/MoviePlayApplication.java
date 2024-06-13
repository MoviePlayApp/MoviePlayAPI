package ar.edu.uade.moviePlay;

import ar.edu.uade.moviePlay.util.ScopeUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class MoviePlayApplication {

	public static void main(String[] args) {
		ScopeUtils.calculateScopeSuffix();
		new SpringApplicationBuilder(MoviePlayApplication.class).registerShutdownHook(true).run(args);
	}

}
