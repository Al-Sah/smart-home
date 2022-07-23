package org.smarthome.controlpanel.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Al-Sah
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "aliases")
@AllArgsConstructor
@RequiredArgsConstructor
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		DeviceAlias that = (DeviceAlias) o;
		return id != null && Objects.equals(id, that.id);
	}
}