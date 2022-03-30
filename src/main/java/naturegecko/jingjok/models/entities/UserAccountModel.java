package naturegecko.jingjok.models.entities;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserAccountModel {

	@Id
	private String accountId;

	private String firstName;
	private String lastName;
	private String nickName;

	private String email;
	private String phoneNumber;

	@JsonIgnore
	private String password;
	
	

}
