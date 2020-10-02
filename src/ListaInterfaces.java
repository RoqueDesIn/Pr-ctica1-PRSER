import java.util.ArrayList;

public class ListaInterfaces {
	//estados
	ArrayList<Interface> listaInterfaces;

	public ListaInterfaces() {
		this.listaInterfaces = new ArrayList <Interface>();
	}
	
	//Comportamientos
	public void addInterface (Interface miInterface){
		listaInterfaces.add(miInterface);
	}

	public void clear() {
		listaInterfaces.clear();	
	}

	public int count() {

		return listaInterfaces.size();
	}

	public ArrayList<Interface> getListaInterfaces() {
		return listaInterfaces;
	}


	
}
