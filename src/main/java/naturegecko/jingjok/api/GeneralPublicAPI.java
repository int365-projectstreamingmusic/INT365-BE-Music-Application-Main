package naturegecko.jingjok.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/general/")
public class GeneralPublicAPI {
	
	@PostMapping
	public ResponseEntity<?> createNewUser(){
		return null;
	}
}
