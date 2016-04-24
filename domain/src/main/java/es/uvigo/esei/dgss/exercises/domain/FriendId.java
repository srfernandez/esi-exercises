package es.uvigo.esei.dgss.exercises.domain;

import java.io.Serializable;

public class FriendId implements Serializable {
	private String friend1;
	private String friend2;

	public String getFriend1() {
		return friend1;
	}

	public String getFriend2() {
		return friend2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((friend1 == null) ? 0 : friend1.hashCode());
		result = prime * result + ((friend2 == null) ? 0 : friend2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FriendId other = (FriendId) obj;
		if (friend1 == null) {
			if (other.friend1 != null)
				return false;
		} else if (!friend1.equals(other.friend1))
			return false;
		if (friend2 == null) {
			if (other.friend2 != null)
				return false;
		} else if (!friend2.equals(other.friend2))
			return false;
		return true;
	}
}