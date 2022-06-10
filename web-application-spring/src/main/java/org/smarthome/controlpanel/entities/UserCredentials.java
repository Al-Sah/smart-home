package org.smarthome.controlpanel.entities;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Al-Sah
 */
@Data
@Entity
@Table(name = "credentials")
public class UserCredentials {


	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	private String login;
	private String pwd;
	private String role;

}