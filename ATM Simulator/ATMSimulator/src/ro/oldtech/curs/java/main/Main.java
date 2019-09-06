package ro.oldtech.curs.java.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import ro.oldtech.curs.java.beans.Client;
import ro.oldtech.curs.java.beans.ClientBanca;
import ro.oldtech.curs.java.beans.ClientBancaIstoric;
import ro.oldtech.curs.java.beans.ContBancar;
import ro.oldtech.curs.java.enums.EnumStariiTranzactii;
import ro.oldtech.curs.java.sm.LocalDB;
import ro.oldtech.curs.java.util.UtilGenerator;

public class Main {

	private static int ERROR_CONTOR = 0;
	private static int LIMIT_ERROR_CONTOR = 3;
	private static String EXPORT_PATH = "export/";

	public final static Map<Integer, ClientBanca> LISTACLIENTBANCA = new LinkedHashMap<>();

	public static void main(String[] args) {

		StringBuilder sb = new StringBuilder();
		sb.append("*********************************************************************").append("\n");
		sb.append("\t\tBUN VENIT IN TERMINALUL ATM").append("\n");
		sb.append("\t\tSELECTEAZA DIN MENIUL DE MAI JOS").append("\n");
		sb.append("*********************************************************************").append("\n");
		System.out.println(sb.toString());

		arataMeniuPrincipal();

	}

	private static void afisajMeniuPrincipal() {
		StringBuilder sb = new StringBuilder();
		sb.append("*********************************************************************").append("\n");
		sb.append("1. AFISARE GLOBALA CONT/CLIENT").append("\n");
		sb.append("2. CREARE CONT/CLIENT").append("\n");
		sb.append("3. EXTRAGE DIN CONT/CLIENT").append("\n");
		sb.append("4. DEPUNERE IN CONT/CLIENT").append("\n");
		sb.append("5. STERGERE CONT/CLIENT").append("\n");
		sb.append("6. UPDATARE INFORMATII CONT/CLIENT").append("\n");
		sb.append("7. SCHIMBARE PIN CONT/CLIENT").append("\n");
		sb.append("8. RAPORT TRANZACTII").append("\n");
		sb.append("9. IESIRE DIN APLICATIE").append("\n");
		sb.append("10. EXPORT EXCEL").append("\n");
		sb.append("11. EXPORT PDF").append("\n");
		sb.append("*********************************************************************").append("\n");
		System.out.println(sb.toString());
	}

	private static void arataMeniuPrincipal() {

		intializareBazaDeDateLocala();

		afisajMeniuPrincipal();

		// logica meniu principal
		Scanner in = new Scanner(System.in);
		while (true) {

			switch (in.nextLine()) {

			case "IESIRE":
				ERROR_CONTOR = 0;
				System.out.println("Comanda IESIRE a fost introdusa. Va multumim ca ati folosit serviciile noastre.");
				System.exit(0);
				break;

			case "1":
				ERROR_CONTOR = 0;
				afiseazaTotiClienti();
				afisajMeniuPrincipal();
				break;

			case "2":
				ERROR_CONTOR = 0;
				creazaContClientNou();
				afisajMeniuPrincipal();
				break;

			case "3":
				ERROR_CONTOR = 0;
				extrageDinCont();
				afisajMeniuPrincipal();
				break;

			case "4":
				ERROR_CONTOR = 0;
				depunereInCont();
				afisajMeniuPrincipal();
				break;

			case "5":
				ERROR_CONTOR = 0;
				deleteDinCont();
				afisajMeniuPrincipal();
				break;

			case "6":
				ERROR_CONTOR = 0;
				updateClientInfo();
				afisajMeniuPrincipal();
				break;

			case "7":
				ERROR_CONTOR = 0;
				updatePin();
				afisajMeniuPrincipal();
				break;

			case "8":
				ERROR_CONTOR = 0;
				afiseazaRaport();
				afisajMeniuPrincipal();
				break;

			case "9":
				ERROR_CONTOR = 0;
				System.out.println("Comanda IESIRE a fost introdusa. Va multumim ca ati folosit serviciile noastre.");
				System.exit(0);
				break;

			case "10":
				// https://poi.apache.org/spreadsheet/examples.html#hssf-only
				ERROR_CONTOR = 0;
				try {
					exportIntoExcel();
				} catch (IOException e) {
					System.err.println("Exceptie aruncata la exportul de excel " + e);
				}
				afisajMeniuPrincipal();
				break;

			case "11":
				// https://developers.itextpdf.com/content/zugferd-future-invoicing/2-creating-pdfa-files-itext
				ERROR_CONTOR = 0;
				try {
					exportIntoPdf();
				} catch (FileNotFoundException | DocumentException e) {
					System.err.println("Exceptie aruncata la exportul de pdf " + e);
				}
				afisajMeniuPrincipal();
				break;

			default:
				System.out.println("Comanda introdusa este gresit sau nerecunoscuta de catre sistem.");
				ERROR_CONTOR++;
				if (ERROR_CONTOR > LIMIT_ERROR_CONTOR) {
					System.out.println(
							"Ai introdus 3 comenzi eronate succesiv. Ai fost scos din aplicatie. Va multumim.");
					System.exit(0);
				}
				break;
			}

		}
	}

	private static void exportIntoPdf() throws FileNotFoundException, DocumentException {

		String pathName = EXPORT_PATH.concat("export.pdf");

		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(pathName));
		document.open();
		document.add(new Paragraph(
				"NUME SI PRENUME | BANCA | SUMA ACTUALA | DATA TRANZAZCTIE | TIP TRANZACTIE | PIN | CONT BANCA"));

		for (Map.Entry<Integer, ClientBanca> mapEntry : LISTACLIENTBANCA.entrySet()) {
			ClientBanca clientBanca = mapEntry.getValue();
			for (ClientBancaIstoric istoric : clientBanca.getListIstoricTranzactii()) {
				document.add(new Paragraph(clientBanca.getClient().getNume().concat(" | ")
						.concat(clientBanca.getContBancar().getNumeBanca()).concat(" | ")
						.concat(Double.toString(clientBanca.getSumaDisponibile())).concat(" | ")
						.concat(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(istoric.getDataTranzactie()))
						.concat(" | ").concat(istoric.getStariTranzactii().name()).concat(" | ")
						.concat(Integer.toString(istoric.getClientBanca().getCodPin())).concat(" | ")
						.concat(istoric.getClientBanca().getContBancar().getContBanca())));
			}
		}

		document.close();

		System.out.println("Fisierul a fost exportat in calea " + pathName + ".");
	}

	private static void exportIntoExcel() throws IOException {

		String[] excelHeader = { "NUME SI PRENUME CLIENT", "BANCA", "SUMA ACTUALA", "DATA TRANZAZCTIE",
				"TIP TRANZACTIE", "PIN", "CONT BANCA" };

		// creare fisier xlsx
		Workbook workbook = new XSSFWorkbook();

		// permite formatare textului adaugare format rich text etc
		CreationHelper createHelper = workbook.getCreationHelper();

		// Create a Sheet
		Sheet sheet = workbook.createSheet("Istoric Tranzactii");

		// adaugam un font
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		// cream un style sheet
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		// Create a Row
		Row headerRow = sheet.createRow(0);

		// cream headerul
		for (int i = 0; i < excelHeader.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(excelHeader[i]);
			cell.setCellStyle(headerCellStyle);
		}

		int rowNum = 1;

		for (Map.Entry<Integer, ClientBanca> mapEntry : LISTACLIENTBANCA.entrySet()) {
			ClientBanca clientBanca = mapEntry.getValue();

			// format special pentru data
			CellStyle dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));

			for (ClientBancaIstoric istoric : clientBanca.getListIstoricTranzactii()) {
				Row row = sheet.createRow(rowNum);
				row.createCell(0).setCellValue(clientBanca.getClient().getNume());
				row.createCell(1).setCellValue(clientBanca.getContBancar().getNumeBanca());
				row.createCell(2).setCellValue(clientBanca.getSumaDisponibile());

				Cell cellDataTranzactie = row.createCell(3);
				cellDataTranzactie.setCellValue(istoric.getDataTranzactie());
				cellDataTranzactie.setCellStyle(dateCellStyle);

				row.createCell(4).setCellValue(istoric.getStariTranzactii().name());
				row.createCell(5).setCellValue(istoric.getClientBanca().getCodPin());
				row.createCell(6).setCellValue(istoric.getClientBanca().getContBancar().getContBanca());

				rowNum++;
			}

		}

		// sa aranjam frumos in sheet-ul de excel coloanele autosize
		for (int i = 0; i < excelHeader.length; i++) {
			sheet.autoSizeColumn(i);
		}

		String pathName = EXPORT_PATH.concat("export.xlsx");
		FileOutputStream fileOut = new FileOutputStream(pathName);

		workbook.write(fileOut);

		fileOut.close();
		workbook.close();

		System.out.println("Fisierul a fost exportat in calea " + pathName + ".");

	}

	private static void updateClientInfo() {
		// 1. Alegem la cine modificam informatii de tip client
		System.out.println("Introdu pin aferente unui client: ");
		Scanner in = new Scanner(System.in);
		int pinIntrodus = Integer.parseInt(in.nextLine());

		// 2. Identificam daca avem un client cu pin-ul introdus disponibil
		ClientBanca clientBanca = LISTACLIENTBANCA.get(pinIntrodus);
		if (clientBanca == null) {
			System.out.println("Nu s-au gasit inregistrati pentru clientul cu pin-ul " + pinIntrodus);
			return;
		}

		System.out.println(afisareMeniuClient());
		boolean out = false;
		while (true && !out) {
			in = new Scanner(System.in);
			int meniuSelectat = in.nextInt();
			switch (meniuSelectat) {
			case 1:
				updateClientName(clientBanca.getClient(), clientBanca);
				break;
			case 2:
				updateClientTelefon(clientBanca.getClient(), clientBanca);
				break;
			case 3:
				updateClientAdresa(clientBanca.getClient(), clientBanca);
				break;
			case 4:
				updateClientBancaNume(clientBanca.getClient(), clientBanca);
				break;
			case 5:
				updateClientBancaCont(clientBanca.getClient(), clientBanca);
				break;
			case 9:
				out = true;
				break;
			default:
				// TODO ce se intampla cand introduce o tasta gresita
				break;
			}
		}
	}

	private static void updateClientBancaCont(Client client, ClientBanca clientBanca) {
		System.out.println("Clientul are CONTUL :" + clientBanca.getContBancar().getContBanca());
		String contNou = UtilGenerator.generateContBancar(clientBanca.getContBancar().getNumeBanca());
		clientBanca.getContBancar().setContBanca(contNou);
		System.out.println("Noul cont este " + clientBanca.getContBancar().getContBanca());
	}

	private static void updateClientBancaNume(Client client, ClientBanca clientBanca) {
		System.out.println("Clientul are BANCA cu numele :" + clientBanca.getContBancar().getNumeBanca());
		System.out.println("Introdu noul nume al BANCII ");
		Scanner in = new Scanner(System.in);
		String numeNou = in.nextLine();
		clientBanca.getContBancar().setNumeBanca(numeNou);
		System.out.println("Noul nume al BANCII este " + clientBanca.getContBancar().getNumeBanca());
		LocalDB localDb = LocalDB.getInstance();
		localDb.getQueue().add(clientBanca);
	}

	private static void updateClientAdresa(Client client, ClientBanca clientBanca) {

	}

	private static void updateClientTelefon(Client client, ClientBanca clientBanca) {

	}

	private static void updateClientName(Client client, ClientBanca clientBanca) {
		System.out.println("Introdu nou nume al clientului " + client.getNume());
		Scanner in = new Scanner(System.in);
		String numeNou = in.nextLine();
		client.setNume(numeNou);
		System.out.println("Noul nume este " + client.getNume());
		LocalDB localDb = LocalDB.getInstance();
		localDb.getQueue().add(clientBanca);
	}

	public static String afisareMeniuClient() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Meniu schimbare informatii client").append("\n");
		sb.append("1. Schimbare nume client").append("\n");
		sb.append("2. Schimbare telefon client").append("\n");
		sb.append("3. Schimbare adresa client").append("\n");
		sb.append("4. Schimbare nume banca").append("\n");
		sb.append("5. Schimbare cont banca").append("\n");
		sb.append("9. Revenire la meniul anterior").append("\n");
		return sb.toString();
	}

	private static void updatePin() {
		// 1. Alegem la cine modificam pin-ul respectiv
		System.out.println("Introdu pin aferente unui client: ");
		Scanner in = new Scanner(System.in);
		int pinIntrodus = Integer.parseInt(in.nextLine());

		// 2. Identificam daca avem un client cu pin-ul introdus disponibil
		ClientBanca clientBanca = LISTACLIENTBANCA.get(pinIntrodus);
		if (clientBanca == null) {
			System.out.println("Nu s-au gasit inregistrati pentru clientul cu pin-ul " + pinIntrodus);
			return;
		}

		// 3. Modificam pinul
		int pinOld = clientBanca.getCodPin();
		System.out.println("Va rog sa introduceti noul pin de la tastatura: ");
		in = new Scanner(System.in);
		int newPin = Integer.parseInt(in.nextLine());

		// verificare daca exista pin-ul nou introdus de la tastatura
		boolean found = false;

		while (true) {
			ClientBanca clientBanca2 = LISTACLIENTBANCA.get(newPin);

			if (clientBanca2 != null) {
				found = true;
			} else {
				found = false;
				break;
			}

			if (found) {
				System.out.println("Va rog sa introduceti un alt pin de la tastatura: ");
				in = new Scanner(System.in);
				newPin = Integer.parseInt(in.nextLine());
			}
		}

		if (!found) {
			ContBancar contBancar = clientBanca.getContBancar();
			contBancar.setPin(newPin);
			clientBanca.setCodPin(newPin);

			LISTACLIENTBANCA.remove(pinOld);
			LISTACLIENTBANCA.put(newPin, clientBanca);

			LocalDB localDb = LocalDB.getInstance();
			localDb.changePin(pinOld, clientBanca);
		}

	}

	private static void intializareBazaDeDateLocala() {
		LocalDB localDb = LocalDB.getInstance();
		localDb.loadAllInfoFromDbLocal();
	}

	private static void afiseazaTotiClienti() {
		final StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, ClientBanca> listare : LISTACLIENTBANCA.entrySet()) {

			ClientBanca clientBanca = listare.getValue();

			sb.append("Nume client: ").append(clientBanca.getClient().getNume()).append(", ");
			sb.append("PIN client: ").append(clientBanca.getContBancar().getPin()).append(" {");
			sb.append("Client : ").append(clientBanca.getClient().toString()).append(" \n ");
			sb.append("Banca : ").append(clientBanca.getContBancar().toString());

			sb.append("}").append("\n");

		}
		System.out.println(sb.toString());

	}

	private static void deleteDinCont() {
		System.out.println("Introdu pin aferente unui client: ");
		Scanner in = new Scanner(System.in);
		int pinIntrodus = Integer.parseInt(in.nextLine());
		ClientBanca clientBanca = LISTACLIENTBANCA.get(pinIntrodus);
		if (clientBanca == null) {
			System.out.println("Nu s-au gasit inregistrati pentru clientul cu pin-ul " + pinIntrodus);
			return;
		}
		LISTACLIENTBANCA.remove(pinIntrodus);

		LocalDB localdb = LocalDB.getInstance();
		localdb.deleteClientInfo(pinIntrodus);
	}

	private static void depunereInCont() {
		System.out.println("Introdu pin aferente unui client: ");
		Scanner in = new Scanner(System.in);
		int pinIntrodus = Integer.parseInt(in.nextLine());
		ClientBanca clientBanca = LISTACLIENTBANCA.get(pinIntrodus);
		if (clientBanca == null) {
			System.out.println("Nu s-au gasit inregistrati pentru clientul cu pin-ul " + pinIntrodus);
			return;
		}

		System.out.println("Introdu suma care doresti sa o depui (X.X)");
		in = new Scanner(System.in);
		double sumaDeDepus = Double.parseDouble(in.nextLine());
		clientBanca.setSumaDisponibile(clientBanca.getSumaDisponibile() + sumaDeDepus);
		System.out.println("Suma dorita a fost depusa in cont " + clientBanca.getSumaDisponibile());

		ClientBancaIstoric istoric = new ClientBancaIstoric();
		istoric.setClientBanca(clientBanca);
		istoric.setStariTranzactii(EnumStariiTranzactii.DEPUNERE);
		istoric.setDataTranzactie(new Date());
		clientBanca.getListIstoricTranzactii().add(istoric);
		saveToDB(clientBanca);
	}

	private static void extrageDinCont() {
		System.out.println("Introdu pin aferente unui client: ");
		Scanner in = new Scanner(System.in);
		int pinIntrodus = Integer.parseInt(in.nextLine());
		ClientBanca clientBanca = LISTACLIENTBANCA.get(pinIntrodus);
		if (clientBanca == null) {
			System.out.println("Nu s-au gasit inregistrati pentru clientul cu pin-ul " + pinIntrodus);
		} else {
			System.out.println("Introdu suma care doresti sa o extragi (X.X)");
			in = new Scanner(System.in);
			double sumaDeExtras = Double.parseDouble(in.nextLine());

			if (sumaDeExtras > clientBanca.getSumaDisponibile()) {
				System.out.println(
						"Suma dorita nu poate fi extrasa deaorece mai ai in cont " + clientBanca.getSumaDisponibile());
				return;
			}

			if (sumaDeExtras > clientBanca.getSumaMaximaRetragere()) {
				System.out.println("Suma dorita nu poate fi extrasa deaorece ai depasit limita maxima de extragere "
						+ clientBanca.getSumaMaximaRetragere());
				return;
			}

			clientBanca.setSumaDisponibile(clientBanca.getSumaDisponibile() - sumaDeExtras);
			System.out.println(
					"Ai extras suma de " + sumaDeExtras + ", mai ai disponibil " + clientBanca.getSumaDisponibile());

			ClientBancaIstoric istoric = new ClientBancaIstoric();
			istoric.setClientBanca(clientBanca);
			istoric.setStariTranzactii(EnumStariiTranzactii.EXTRAGERE);
			istoric.setDataTranzactie(new Date());
			clientBanca.getListIstoricTranzactii().add(istoric);
			saveToDB(clientBanca);

		}
	}

	private static void afiseazaRaport() {
		System.out.println("Introdu pin aferente unui client: ");
		Scanner in = new Scanner(System.in);
		int pinIntrodus = Integer.parseInt(in.nextLine());

		ClientBanca clientBanca = LISTACLIENTBANCA.get(pinIntrodus);

		if (clientBanca == null) {
			System.out.println("Nu s-au gasit inregistrati pentru clientul cu pin-ul " + pinIntrodus);
		} else {

			final StringBuilder sb = new StringBuilder();
			sb.append("Afisare istoric operatiuni pentru " + clientBanca.getClient().getNume()).append("\n");
			for (ClientBancaIstoric istoric : clientBanca.getListIstoricTranzactii()) {

				sb.append("Nume client: ").append(clientBanca.getClient().getNume()).append(", DATA: ")
						.append(UtilGenerator.formatDate(istoric.getDataTranzactie())).append(", OPERATIUNE: ")
						.append(istoric.getStariTranzactii().name()).append("\n");

			}
			System.out.println(sb.toString());

		}

	}

	private static void creazaContClientNou() {

		Client client = new Client();
		ContBancar contBancar = new ContBancar();
		ClientBanca clientBanca = new ClientBanca();

		System.out.println("Introdu numele clientului: ");
		Scanner in = new Scanner(System.in);
		String numeClient = in.nextLine();

		System.out.println("Introdu numarul de telefon al clientului: ");
		in = new Scanner(System.in);
		String numarTelefonClient = in.nextLine();

		System.out.println("Introdu CNP-ul clientului: ");
		in = new Scanner(System.in);
		String cnpClient = in.nextLine();

		System.out.println("Introdu adresa clientului: ");
		in = new Scanner(System.in);
		String adresa = in.nextLine();

		client.setNume(numeClient);
		client.setNumarDeTelefon(numarTelefonClient);
		client.setCnp(cnpClient);
		client.setAdresa(adresa);

		System.out.println("Client inregistrat in sistem.");
		System.out.println(client.toString());

		String numeBanca = UtilGenerator.getRandomNumeBanca();

		contBancar.setCodCard(UtilGenerator.generateCodCard());
		contBancar.setCodCvc(UtilGenerator.generateCodCvc());
		contBancar.setContBanca(UtilGenerator.generateContBancar(numeBanca));
		contBancar.setNumeBanca(numeBanca);
		contBancar.setPin(UtilGenerator.generatePinCode());
		contBancar.setZiiExpirare(UtilGenerator.generateZii());
		contBancar.setLunaExpirare(UtilGenerator.generateLuna());

		System.out.println("Informatii bancare generate.");
		System.out.println(contBancar.toString());

		clientBanca.setClient(client);
		clientBanca.setContBancar(contBancar);

		in = new Scanner(System.in);
		System.out.println("Introdu suma de bani pe care doresti sa o stochezi in cont (X.X).");
		String suma = in.nextLine();
		clientBanca.setSumaDisponibile(Double.parseDouble(suma));
		clientBanca.setSumaMaximaRetragere(UtilGenerator.generateLimitaExtractie());

		System.out.println("Informatii sume:");
		System.out.println(clientBanca.toString());

		ClientBancaIstoric istoric = new ClientBancaIstoric();
		istoric.setClientBanca(clientBanca);
		istoric.setStariTranzactii(EnumStariiTranzactii.DEPUNERE);
		istoric.setDataTranzactie(new Date());
		clientBanca.setCodPin(contBancar.getPin());
		clientBanca.getListIstoricTranzactii().add(istoric);

		LISTACLIENTBANCA.put(contBancar.getPin(), clientBanca);
		saveToDB(clientBanca);

	}

	private static void saveToDB(ClientBanca clientBanca) {
		LocalDB local = LocalDB.getInstance();
		local.getQueue().add(clientBanca);
	}

}
