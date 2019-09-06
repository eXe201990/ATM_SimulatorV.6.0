package ro.oldtech.curs.java.beans;

import java.io.Serializable;

public class ContBancar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String numeBanca;
	private String contBanca;

	private int pin;
	private String codCard;
	private int codCvc;
	private int ziiExpirare;
	private int lunaExpirare;

	public ContBancar() {
		// TODO Auto-generated constructor stub
	}

	public String getNumeBanca() {
		return numeBanca;
	}

	public void setNumeBanca(String numeBanca) {
		this.numeBanca = numeBanca;
	}

	public String getContBanca() {
		return contBanca;
	}

	public void setContBanca(String contBanca) {
		this.contBanca = contBanca;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public String getCodCard() {
		return codCard;
	}

	public void setCodCard(String codCard) {
		this.codCard = codCard;
	}

	public int getCodCvc() {
		return codCvc;
	}

	public void setCodCvc(int codCvc) {
		this.codCvc = codCvc;
	}

	public int getZiiExpirare() {
		return ziiExpirare;
	}

	public void setZiiExpirare(int ziiExpirare) {
		this.ziiExpirare = ziiExpirare;
	}

	public int getLunaExpirare() {
		return lunaExpirare;
	}

	public void setLunaExpirare(int lunaExpirare) {
		this.lunaExpirare = lunaExpirare;
	}

	@Override
	public String toString() {

		final StringBuilder sb = new StringBuilder();
		sb.append("NUME BANCA: ").append(numeBanca).append("\n");
		sb.append("CONT BANCA: ").append(contBanca).append("\n");
		sb.append("COD CARD: ").append(codCard).append("\n");
		sb.append("COD CVC: ").append(codCvc).append("\n");
		sb.append("DATA ZI: ").append(ziiExpirare).append("\n");
		sb.append("DATA LUNA: ").append(lunaExpirare).append("\n");
		sb.append("COD PIN: ").append(pin).append("\n");

		return sb.toString();

	}

}
