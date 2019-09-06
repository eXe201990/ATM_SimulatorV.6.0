package ro.oldtech.curs.java.beans;

import java.io.Serializable;
import java.util.Date;

import ro.oldtech.curs.java.enums.EnumStariiTranzactii;

public class ClientBancaIstoric implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3313703271967266341L;
	private ClientBanca clientBanca;
	private Date dataTranzactie;
	private EnumStariiTranzactii stariTranzactii;

	public ClientBancaIstoric() {
		// TODO Auto-generated constructor stub
	}

	public ClientBanca getClientBanca() {
		return clientBanca;
	}

	public void setClientBanca(ClientBanca clientBanca) {
		this.clientBanca = clientBanca;
	}

	public EnumStariiTranzactii getStariTranzactii() {
		return stariTranzactii;
	}

	public void setStariTranzactii(EnumStariiTranzactii stariTranzactii) {
		this.stariTranzactii = stariTranzactii;
	}

	public Date getDataTranzactie() {
		return dataTranzactie;
	}

	public void setDataTranzactie(Date dataTranzactie) {
		this.dataTranzactie = dataTranzactie;
	}

}
