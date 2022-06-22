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
@Table(name = "profiles")
@AllArgsConstructor
@RequiredArgsConstructor
public class ProfileInfo {


	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;

	private String telNumber;
	private String email;
	private String name;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ProfileInfo that = (ProfileInfo) o;
		return id != null && Objects.equals(id, that.id);
	}
}