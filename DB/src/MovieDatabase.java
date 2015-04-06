public class MovieDatabase {
	MyLinkedList<Genre> genres;

	public MovieDatabase() {
		// FIXME implement this
		// Maintain a linked list of Genre using MyLinkedList
		this.genres = new MyLinkedList<Genre>();
	}

	public void insert(String genre, String title) {
		// FIXME implement this
		// Insert the given genre and title to the MovieDatabase.
		// Printing functionality is provided for the sake of debugging.
		// This code should be removed before submitting your work.
		Genre g = new Genre(genre);
		this.genres.add(g);
		this.genres.find(g).titles.add(title);
		//System.err.printf("[trace] INSERT [%s] [%s]\n", genre, title);
	}

	public void delete(String genre, String title) {
		// FIXME implement this
		// Remove the given genre and title from the MovieDatabase.
		// Printing functionality is provided for the sake of debugging.
		// This code should be removed before submitting your work.
		Genre g = new Genre(genre);
		if ((g = this.genres.find(g))!=null) {
			if(g.titles.remove(title)){
			if (this.genres.find(g).titles.size() == 0)
				this.genres.remove(g);
			}
		}
		//System.err.printf("[trace] DELETE [%s] [%s]\n", genre, title);
	}

	public MyLinkedList<QueryResult> search(String term) {
		// FIXME implement this
		// Search the given term from the MovieDatabase.
		// You should return a linked list of QueryResult.
		// The search command is handled at SearchCmd.java.
		// Printing functionality is provided for the sake of debugging.
		// This code should be removed before submitting your work.
		//System.err.printf("[trace] SEARCH [%s]\n", term);

		MyLinkedList<QueryResult> results = new MyLinkedList<QueryResult>();
		MyLinkedListIterator<Genre> i = (MyLinkedListIterator<Genre>) this.genres
				.iterator();
		while (i.hasNext()) {
			Genre g = i.next();
			MyLinkedListIterator<String> j = (MyLinkedListIterator<String>) g.titles
					.iterator();
			while (j.hasNext()) {
				String s = j.next();
				if (term == null || s.contains(term))
					results.add(new QueryResult(g.name, s));
			}
		}
		return results;
	}
}