// Why we need such as User class? To sort User based on the score (similarity). 
package offline;

public class User implements Comparable<User> {
	private String id;
	private Double value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public User(String id, Double value) {
		this.id = id;
		this.value = value;
	}
	// if other.value > this.value means this has lower priority
	@Override
	public int compareTo(User other) {
		return (int) (other.value - this.value);
	}
}
