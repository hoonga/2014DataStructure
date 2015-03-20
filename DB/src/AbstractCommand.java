public abstract class AbstractCommand implements Command {
	@Override
	public void apply(MovieDatabase db, String args) throws DatabaseException {
		String[] arga = parse(args);
		queryDatabase(db, arga);
	}

	private String[] parse(String args) throws CommandParseException {
		if (args.isEmpty()) {
			return new String[] {};
		} else {
			args.replaceAll("%", "");
			return args.split(" ");
		}
	}

	protected abstract void queryDatabase(MovieDatabase db, String[] arga) throws DatabaseException;
}
