package ch.hearc.parapa_II.labo;

public class Log {
	public enum Type {
		WAITING, REMOVE, FINISHED
	}

	private Type type;
	private Person person;
	private long time;

	/**
	 * Constructor
	 * 
	 * @param type   Type of the log
	 * @param person Person logged
	 */
	public Log(Type type, Person person, long time) {
		this.type = type;
		this.person = person;
		this.time = time;
	}

	// Getters

	public Type getType() {
		return type;
	}

	public Person getPerson() {
		return person;
	}

	public long getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "Log [type=" + type + ", person=" + person + ", time=" + time + "]";
	}
}
