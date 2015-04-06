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
			args = args.trim();
			args = args.replaceFirst(" {0,}%", "");
			args = args.substring(0, args.length()-1);
			return args.split("% {0,}%");
		}
	}

	protected abstract void queryDatabase(MovieDatabase db, String[] arga) throws DatabaseException;
}
