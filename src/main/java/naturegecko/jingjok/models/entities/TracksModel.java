package naturegecko.jingjok.models.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tracks", schema = "playmylist")
@AllArgsConstructor
@NoArgsConstructor
public class TracksModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int track_id;
	
	private String track_file;
	private String track_name;
	private Date timestamp;
	private int duration;
	private String track_desc;
	
	@Nullable
	private String thumbnail;
	private int view_count;
	private int account_id;

}
