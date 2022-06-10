package org.smarthome.controlpanel.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Al-Sah
 */
@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	private String login;

	private String pwd;

	private String role;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "profile_info_id", referencedColumnName = "id")
	private ProfileInfo info;


	@OneToMany(mappedBy="user")
	private Set<DeviceAlias> aliases;

}