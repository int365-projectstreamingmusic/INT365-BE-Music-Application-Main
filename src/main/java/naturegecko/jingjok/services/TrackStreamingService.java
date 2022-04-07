package naturegecko.jingjok.services;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class TrackStreamingService {
	
	@Autowired
	private MinioStorageService minioStorageService;
	
	
	@GetMapping(value = "/lelel", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@SneakyThrows
	public InputStream playAutio(HttpServletRequest request, HttpServletResponse response) {
		InputStream GetMusic = minioStorageService.trackRetrivelService("testingsite/testmusic112.mp3");
		GetMusic.close();
		return GetMusic;
	}

	/*
	 * 
	 * @RequestMapping(value = "/sound/character/get/{characterId}", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_OCTET_STREAM_VALUE })
        public ResponseEntity playAudio(HttpServletRequest request,HttpServletResponse response, @PathVariable("characterId") int characterId) throws FileNotFoundException{

        logger.debug("[downloadRecipientFile]");

        de.tki.chinese.entity.Character character = characterRepository.findById(characterId);
        String file = UPLOADED_FOLDER + character.getSoundFile();

        long length = new File(file).length();


        InputStreamResource inputStreamResource = new InputStreamResource( new FileInputStream(file));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentLength(length);
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        return new ResponseEntity(inputStreamResource, httpHeaders, HttpStatus.OK);
    }
	 */
}
