package org.smarthome.controlpanel.entities;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Al-Sah
 */
@Data
@Entity
@Table(name = "aliases")
public class DeviceAlias {


	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	private Integer user;
	private String device;
	private String name;

}