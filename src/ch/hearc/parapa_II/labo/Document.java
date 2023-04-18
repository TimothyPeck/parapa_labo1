package ch.hearc.parapa_II.labo;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Document implements Comparable<Document> {
	/*
	 * -----------------------------------------------------------------------------
	 * ------
	 * DONE : Ajouter les locks au documents afin d'assurer un acces concurrent Ã
	 * celui-ci
	 *
	 * Remarque : java.util.concurrent contient tout ce qu'il faut
	 * -----------------------------------------------------------------------------
	 * ------
	 */

	// Concurrent content
	private String content;

	// Other variables
	private String name;
	private ReentrantReadWriteLock lock;

	/**
	 * Constructor
	 * 
	 * @param name Name of the document
	 */
	public Document(String name) {
		this.name = name;

		content = "No data";

		this.lock = new ReentrantReadWriteLock(true);
	}

	/**
	 * Get document name
	 * 
	 * @return the name of the document
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get document content, accessed by readers
	 * 
	 * @return the content of the document
	 */
	public String readContent() {
		lock.readLock().lock();
		String value = new String(this.content);
		lock.readLock().unlock();
		return value;
	}

	/**
	 * Set the document's content, accessed by writers
	 * 
	 * @param newContent New content of the document
	 */
	public void setContent(String newContent) {
		lock.writeLock().lock();
		this.content = newContent;
		lock.writeLock().unlock();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Document) {
			Document doc = (Document) obj;
			return doc.getName().equals(this.getName());
		}

		return false;
	}

	@Override
	public int compareTo(Document o) {
		return this.getName().compareTo(o.getName());
	}
}