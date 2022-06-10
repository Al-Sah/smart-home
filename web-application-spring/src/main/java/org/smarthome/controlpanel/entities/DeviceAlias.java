package org.smarthome.controlpanel.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Al-Sah
 */
@Data
@Entity
@Table(name = "aliases")
@NoArgsConstructor
public class DeviceAlias {


	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	private Integer user;
	private String device;
	private String name;

	public DeviceAlias(Integer user, String device, String name) {
		this.user = user;
		this.device = device;
		this.name = name;
	}
}