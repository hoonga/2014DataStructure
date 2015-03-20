class Genre implements Comparable<Genre> {
	// FIXME implement this
	// Implement a Genre class.
	String name;// This class should hold the name of the genre.
	MyLinkedList<String> list;// This class should maintain a linked list of movie titles for this genre.
	public Genre(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Genre other) {
		return this.name.compareTo(other.name);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (! (other instanceof Genre)) return false;

		// TODO implement by yourself to test equality,
		// or use 'Source' - 'Generate hashCode() and equals() ...'
		return true;
	}

	@Override
	public int hashCode() {
		// TODO implement by yourself,
		// or use 'Source' - 'Generate hashCode() and equals() ...'
		// MAKE SURE THAT THE IMPLEMENTATION IS COMPATIBLE WITH equals()
		return 0;
	}

	@Override
	public String toString() {
		return "";
	}
}
