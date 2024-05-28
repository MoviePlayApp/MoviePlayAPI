// package ar.edu.uade.moviePlay.MovieTests;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import org.springframework.web.context.WebApplicationContext;

// import ar.edu.uade.moviePlay.controller.MovieController;
// import ar.edu.uade.moviePlay.dto.movie.GetMovieDTO;
// import ar.edu.uade.moviePlay.dto.movie.MovieDTO;
// import ar.edu.uade.moviePlay.service.MovieService;

// import java.util.List;

// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(MovieController.class)
// public class MovieControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private MovieService movieService;

//     @Autowired
//     private WebApplicationContext webApplicationContext;

//     @BeforeEach
//     public void setup() {
//         mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//     }

//     @Test
//     public void testGetMovies() throws Exception {
//         MovieDTO movie1 = new MovieDTO();
//         movie1.setId(1);
//         movie1.setImageUri("image1");

//         MovieDTO movie2 = new MovieDTO();
//         movie2.setId(2);
//         movie2.setImageUri("image2");

//         GetMovieDTO getMovieDTO = new GetMovieDTO();
//         getMovieDTO.setMovies(List.of(movie1, movie2));

//         when(movieService.getMovies(1, 30, "", null, null, null)).thenReturn(getMovieDTO);

//         String expectedJson = "{\"movies\":[{\"id\":1,\"imageUri\":\"image1\"},{\"id\":2,\"imageUri\":\"image2\"}]}";

//         mockMvc.perform(get("/movies")
//                 .param("page", "1")
//                 .param("limit", "30")
//                 .param("search", "")
//                 .param("orderByDate", "")
//                 .param("orderByRate", "")
//                 .param("genre", "")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(content().json(expectedJson));
//     }
// }