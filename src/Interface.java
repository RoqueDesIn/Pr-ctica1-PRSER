
public class Interface {
	// estados
	protected String interfaz;
	protected String MAC;
	protected String IP;
	
	//Comportamientos
	/**
	 * Constructor serializado
	 * @param serializado
	 */
	public Interface(String serializado) {
		String escindido[]=serializado.split(";");
		interfaz = escindido[0];
		MAC = escindido[1];
		IP = escindido[2];
	}
	/**
	 * Constructor vacio
	 */
		public Interface() {

	}
		// getters
	public String getInterfaz() {
		return interfaz;
	}
	public String getMAC() {
		return MAC;
	}
	public String getIP() {
		return IP;
	}

	
}
