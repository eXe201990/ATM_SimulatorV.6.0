package ro.oldtech.curs.java.beans;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ClientBanca implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1829974547110480712L;
	private Client client;
	private Integer codPin;
	private ContBancar contBancar;
	private double sumaDisponibile;
	private double sumaMaximaRetragere;
	private List<ClientBancaIstoric> listIstoricTranzactii = new LinkedList<>();

	public ClientBanca() {
		// TODO Auto-generated constructor stub
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public ContBancar getContBancar() {
		return contBancar;
	}

	public void setContBancar(ContBancar contBancar) {
		this.contBancar = contBancar;
	}

	public double getSumaDisponibile() {
		return sumaDisponibile;
	}

	public void setSumaDisponibile(double sumaDisponibile) {
		this.sumaDisponibile = sumaDisponibile;
	}

	public double getSumaMaximaRetragere() {
		return sumaMaximaRetragere;
	}

	public void setSumaMaximaRetragere(double sumaMaximaRetragere) {
		this.sumaMaximaRetragere = sumaMaximaRetragere;
	}

	public List<ClientBancaIstoric> getListIstoricTranzactii() {
		return listIstoricTranzactii;
	}

	public void setListIstoricTranzactii(List<ClientBancaIstoric> listIstoricTranzactii) {
		this.listIstoricTranzactii = listIstoricTranzactii;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("SUMA DISPONIBILA IN CONT: ").append(sumaDisponibile).append("\n");
		sb.append("SUMA MAXIMA DE RETRAS DINTR-O ACTIUNE: ").append(sumaMaximaRetragere).append("\n");

		return sb.toString();
	}

	public Integer getCodPin() {
		return codPin;
	}

	public void setCodPin(Integer codPin) {
		this.codPin = codPin;
	}

}
