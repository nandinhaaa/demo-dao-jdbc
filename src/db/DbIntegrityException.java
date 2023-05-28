package db;
//classe criada para integridade referencial (para n apagar depart que existe vendedores)
public class DbIntegrityException extends RuntimeException {

	public static final long serialVersionUID = 1l;

		public DbIntegrityException(String msg) {
			super(msg);
		}
	}

