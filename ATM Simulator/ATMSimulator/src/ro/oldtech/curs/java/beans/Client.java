package ro.oldtech.curs.java.beans;

import java.io.Serializable;

public class Client implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3695276568373780017L;
	private String nume;
	private String cnp;
	private String adresa;
	private String numarDeTelefon;

	public Client() {
		// TODO Auto-generated constructor stub
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getCnp() {
		return cnp;
	}

	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getNumarDeTelefon() {
		return numarDeTelefon;
	}

	public void setNumarDeTelefon(String numarDeTelefon) {
		this.numarDeTelefon = numarDeTelefon;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Nume client: ").append(nume).append("\n");
		sb.append("CNP client: ").append(cnp).append("\n");
		sb.append("Numar telefon client: ").append(numarDeTelefon).append("\n");
		sb.append("Adresa client: ").append(adresa).append("\n");

		return sb.toString();
	}

}
