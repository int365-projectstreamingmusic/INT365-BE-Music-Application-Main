package naturegecko.jingjok.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.minio.StatObjectResponse;
import naturegecko.jingjok.repositories.UserAccountsRepository;
import naturegecko.jingjok.services.BucketCommunicationService;
import naturegecko.jingjok.services.MinioStorageService;

@RestController
@RequestMapping("/api/general/")
public class GeneralPublicAPI {
	
	@Autowired
	private UserAccountsRepository accountsRepository;

	@PostMapping
	public ResponseEntity<?> createNewUser() {
		return null;
	}
	
	@GetMapping("test")
	public String getBucket() {
		return ""+BucketCommunicationService.pingBucket("playmdylist");
	}
	
	@GetMapping
	public ResponseEntity<?> getMysicList() {
		return null;	
	}
	
	//This will return only 1 track.
	@GetMapping("searchByTrackName/{trackName}")
	public ResponseEntity<?> searchByTrackName(@PathVariable("trackName") String trackName){
		Map<String, Object> responseSet = new HashMap<String, Object>();
		
		//HashMap<String,Object>  responseSet = new HashMap();
		responseSet.put("responseStatus", "200");
		responseSet.put("responseMapLeader", "/api/general/"+trackName);
		responseSet.put("responseFileName", trackName);
		responseSet.put("responseObject", accountsRepository.findAll());
		
		//StatObjectResponse statObject = MinioStorageService.get
		
		
	
		
		
		
		
		//responseSet.add("d","dd");
		
		//Set<Object> responseSet;
		
		//responseSet.add(responseSet);
		
		return ResponseEntity.ok().body(responseSet);
	}

}
