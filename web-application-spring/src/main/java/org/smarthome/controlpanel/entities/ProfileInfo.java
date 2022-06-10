package org.smarthome.controlpanel.entities;

import lombok.Data;
import javax.persistence.*;

/**
 * @author Al-Sah
 */
@Data
@Entity
@Table(name = "profiles")
public class ProfileInfo {


	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	private String telNumber;
	private String email;
	private String name;

}