import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InterfaceAddress;
import java.util.Scanner;

public class practica1PRSER {
	//****************
	// estados
	//****************
	// texto par el menú
	private static final String menu = "*******************************\n"+
									   "             MENÚ\n"+
									   "*******************************\n"
			+ "1. Crear una carpeta dada una ruta y el nombre\r\n" + 
			"2. Crear un fichero dada la ruta y el nombre\r\n" + 
			"3. Listar todas las interfaces de red de nuestro ordenador\r\n" + 
			"4. Mostrar la IP del ordenador dado el nombre de la interfaz de red\r\n" + 
			"5. Mostrar la dirección MAC dado el nombre de la interfaz de red\r\n" + 
			"6. Comprobar conectividad con internet\r\n" + 
			"7. Salir";
	//lista de interfaces
	private static 	ListaInterfaces miListaInterfaces=new ListaInterfaces();
	// proceso 
	private static ProcessBuilder processBuilder = new ProcessBuilder();
	// Scanner
	private static	Scanner sc= new Scanner (System.in);
	private static boolean SO=true;
	//******************
	// métodos
	//******************
	/**
	 * método principal de la aplicación
	 * ejecuta el menú en un bucle infinito
	 * @param args
	 */
	public static void main(String[] args) {
		// comprueba SO: true es Windows, False es otro.
		System.out.println("Su sistema operativo es "+System.getProperty("os.name")+"\n");
		if (System.getProperty("os.name").contains("Windows")) {
			SO=true;
		} else {
			SO=false;
		}
		
		// bucle infinito
		while (true) {
			// esribe el menú
			System.out.println(menu);
			// gestiona el menú
			String entrada= sc.nextLine();
			// switch tecla
			switch (entrada) {
			case "1":
				crearCarpeta();
				// espera una tecla
				EsperaTecla();
				break;
			case "2":
				crearFichero();
				// espera una tecla
				EsperaTecla();
				break;
			case "3":
				// ejecuta IPConfig/Ifconfig
				listaIFC();
				// Carga la lista de interfaces
				creaLista(ejecutaProcess());
				// muestra por pantalla las interfaces de la lista
				procesaIp();
				// espera una tecla
				EsperaTecla();
				break;
			case "4":
				// crea el comando IPCONFIG y lo ejecuta
				listaIFC();
				//crea lista de interfaces
				creaLista(ejecutaProcess());
				//Muestra lista de Interfaces
				mostrarIP();
				// espera una tecla
				EsperaTecla();
				break;
			case "5":
				// crea el comando IPCONFIG y lo ejecuta
				listaIFC();
				//crea lista de interfaces
				creaLista(ejecutaProcess());
				//Muestra lista de Interfaces
				mostrarMAC();
				// espera una tecla
				EsperaTecla();
				break;
			case "6":
				// ejecuta el comando Ping
				String resultado=checkConectividad();
				// procesa el resultado del comando ping
				checkPing(resultado);
				// espera una tecla
				EsperaTecla();
				break;
			case "7":
				System.out.println("Programa finalizado por el usuario.");
				System.exit(0);
				break;
			default:
				System.out.println("ha escrito una opción no válida "+entrada);
			}


		}
	}
	
	/**
	 * Comprueba si hay conectividad
	 * devuelve false si no hay conectividad
	 * devuelve true si hay conectividad
	 * @param datos los datos a procesar
	 * @return falso > NO hay conectividad, verdadero SI hay conectividad
	 */
	private static void checkPing(String datos) {
		String []lineas=datos.split("\n");
		boolean resultado=true;

		// guarda en StrinCheck el texto a comparar según el SO
		 String StringCheck=SO ? "100% perdidos":"100% packet loss";
		 String StringCheck2=SO ? "no pudo encontrar el host":"Nombre o servicio desconocido";
		 
		//recorre las lineas del resultado
		for (int i=0;i<lineas.length;i++) {
			if (lineas[i].contains(StringCheck) || lineas[i].contains(StringCheck2)) {
				resultado=false;
			}
		}
		if (resultado){
			System.out.println("La URL/IP solicitada SI tiene conectividad\n");
		} else {
			System.out.println("La URL/IP solicitada NO tiene conectividad\n");
		}
	}

	/**
	 * Ejecuta el comando ping con la url/ip proporcionada por el usuario
	 * @return String con el resultado obtenido del comando
	 */
	private static String checkConectividad() {
		System.out.println("Introduzca la IP ó URL a comprobar:"+"\r\n");
		String URLCheck= sc.nextLine();
		//llama al proceso para Windows
		if (SO) {
			processBuilder.command("cmd.exe", "/c", "ping " + URLCheck);		
		} else {
		// llama al proceso para Linux
			processBuilder.command("bash", "-c", "ping -c 1 "+URLCheck);
		}

		String resultado=ejecutaProcess();
		return resultado;
	}
	
	/**
	 * Muestra la MAC desde la clase ListaInterfaces
	 */
	private static void mostrarMAC() {
		System.out.println("*************************\n Escribe un número de Interfaz para consultar su MAC:\n");
		
		// muestra las interfaces
		for (int i=0;i<miListaInterfaces.count();i++) {
			System.out.println((i+1)+".- "+miListaInterfaces.getListaInterfaces().get(i).getInterfaz());
		}
		String elected= sc.nextLine();
		
		System.out.println("La interfaz "+elected+" con nombre "+
					miListaInterfaces.getListaInterfaces().get(Integer.valueOf(elected)-1).getInterfaz()+" tiene la MAC: "+
					miListaInterfaces.getListaInterfaces().get(Integer.valueOf(elected)-1).getMAC()+"\n");		
	}
	/**
	 * Espera a que el usuario pulse una tecla
	 */
	private static void EsperaTecla() {
		System.out.println("pulse intro para continuar.");
		String ruta= sc.nextLine();	
	}
	/**
	 * muestra por pantalla las interfaces y pide por teclado
	 * una interfaz para mostrar su IP
	 */
	private static void mostrarIP() {
		System.out.println("*************************\n Escribe un número de Interfaz para consultar su IP:\n");
		
		for (int i=0;i<miListaInterfaces.count();i++) {
			System.out.println((i+1)+".- "+miListaInterfaces.getListaInterfaces().get(i).getInterfaz());
		}
		String elected= sc.nextLine();
		System.out.println("La interfaz "+elected+" con nombre "+ 
					miListaInterfaces.getListaInterfaces().get(Integer.valueOf(elected)-1).getInterfaz()+" tiene la IP: "+
					miListaInterfaces.getListaInterfaces().get(Integer.valueOf(elected)-1).getIP()+"\n");
	}
	/**
	 * ejecuta ipconfig 
	 */
	private static void listaIFC() {
		System.out.println("**************************\nLeyendo Interfaces"+"\n**************************\n");

		//llama al proceso para Windows
		if (SO) {
			processBuilder.command("cmd.exe", "/c", "ipconfig /all");		} else {
		// llama al proceso para Linux
			processBuilder.command("bash", "-c", "ifconfig ");
		}
	}
	
	/**
	 * Crea las interfaces y las añade a la lista de interfaces para 
	 * posteriores consultas con los datos proporcionados.
	 * @param String con los datos a procesar
	 */
	private static void creaLista(String datos) {
		String lineas[]=datos.split("\n");
		
		// inicializa variables
		miListaInterfaces.clear();
		String interfaz="No hay Interfaz";
		String IP="No hay IP";
		String MAC="No hay MAC";
		// procesa los Strings a buscar según el SO
		 String SCInterfaz=SO ? "Descripci¢n":"flags";
		 String SCMAC=SO ? "Direcci¢n f¡sica":"ether";
		 String SCIP=SO ? "IPv4":"inet ";
		 String nuevaInterfaz=SO ? "Adaptador":"TX errors";
		 // según SO elige separador
		 String separador=SO ? ":":" ";
		 // contador de interfaces
		 int nInterfaz=0;
		 int interfazcActual=0;
		 int aCompararSO=SO?1:0;		 
		// Recorre las lineas devueltas por el comando
		for (int i=0;i<lineas.length;i++) {
			String campos[]=lineas[i].split(separador);
			// comienzan los datos de un adaptador
	
			// es la linea de interface
			if (lineas[i].contains(SCInterfaz)){
				interfaz=SO?campos[1]:campos[0].substring(0, campos[0].length()-1);
			}
			if (lineas[i].contains(SCMAC)){
				//es la linea de la MAC, en Window es el campo 2, en Linux el 9
				MAC=SO?campos[1]:campos[9];
			}
			if (lineas[i].contains(SCIP)){
				// Es la linea de la IPv4, en Window es el campo 2, en Linux el 9
				MAC=SO?campos[1]:campos[9];
			}
			// cuenta las interfaces
			if (lineas[i].contains(nuevaInterfaz)) {
				nInterfaz=nInterfaz+1;
			}
			// si ha encontrado una nueva Interfaz(Windows) ó ha terminado la anterior(Linux)
			// guarda el objeto interfaz en la lista
			if ((nInterfaz>interfazcActual) && (nInterfaz>aCompararSO)){
				miListaInterfaces.addInterface(new Interface (interfaz+";"+MAC+";"+IP));
				interfazcActual=nInterfaz;
			}
		}
	}
	
	/**
	 * procesa los datos y escribe el resultado de buscar Interface, MAC y IP
	 */
	private static void procesaIp() {
		String resultado="***************************\n Listado de Interfaces\n";
		 
		// Recorre las lineas de la lista y crea el String
		for (int i=0;i<miListaInterfaces.getListaInterfaces().size();i++) {
				resultado=resultado+"***************************\n";
				resultado=resultado+"Interface: "+miListaInterfaces.getListaInterfaces().get(i).getInterfaz()+"\n";
				resultado=resultado+"MAC: "+miListaInterfaces.getListaInterfaces().get(i).getMAC()+"\n";
				resultado=resultado+"IP: "+miListaInterfaces.getListaInterfaces().get(i).getIP()+"\n";
		}
		System.out.println(resultado);
	}
	
	/**
	 * Crea un archivo dada una ruta prporcionada
	 */
	private static void crearFichero() {
		System.out.println("Introduzca la ruta completa del fichero a crear:"+"\r\n");
		String ruta= sc.nextLine();
		
		//llama al proceso para Windows
		if (SO) {
			processBuilder.command("cmd.exe", "/c", "nul > " + ruta);
		} else {
		// llama al proceso para Linux
			processBuilder.command("bash", "-c", "touch " + ruta);
		}
		ejecutaProcess();
	}

	/**
	 * crea una carpeta dada una ruta completa
	 */
	private static void crearCarpeta() {
		System.out.println("Introduzca la ruta completa de la carpeta a crear:"+"\r\n");
		String ruta= sc.nextLine();
		
		//llama al proceso para Windows 
		if (SO) {
			processBuilder.command("cmd.exe", "/c", "mkdir " + ruta);
		} else {
		//llama al proceso para Linux
			processBuilder.command("bash", "-c", "mkdir " + ruta);	
		}
		ejecutaProcess();
	}

	/**
	 * Crea el proceso y devuelve un String con el resultado.
	 */
	private static String ejecutaProcess() {
		String resultado=null;
		try {
			// ejecuta el comando
			Process proceso =processBuilder.start();
			// recoge el resultado en un StrinBuilder
			StringBuilder buffer = new StringBuilder();
			BufferedReader lector = new BufferedReader (new InputStreamReader(proceso.getInputStream()));
			
			//Recoge el resultado en un StringBuffer
			String linea;
			linea =lector.readLine();
			while (linea!=null) {
				buffer.append(linea+"\n");
				linea =lector.readLine();
			}
			resultado= buffer.toString();
			if (proceso.exitValue()==0) {
				System.out.println("Comando ejecutado con éxito.");
			} else {
				System.out.println("El comando ha devuelto un error:");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultado;

//Termina MAIN
	}
	
	
//termina la clase	
}
