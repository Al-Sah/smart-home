package org.smarthome.controlpanel.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * @author Al-Sah
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
@RequiredArgsConstructor
@AllArgsConstructor
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
	@ToString.Exclude
	private Set<DeviceAlias> aliases;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		User user = (User) o;
		return id != null && Objects.equals(id, user.id);
	}
}