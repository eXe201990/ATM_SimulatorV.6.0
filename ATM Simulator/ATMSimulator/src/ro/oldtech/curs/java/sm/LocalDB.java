package ro.oldtech.curs.java.sm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import ro.oldtech.curs.java.beans.ClientBanca;
import ro.oldtech.curs.java.main.Main;

public class LocalDB implements Runnable {

	private static LocalDB singleton;
	private static final Long WAKEUP = 5 * 1000L;
	private Thread thread;
	private String UID = Long.toString(new Date().getTime());
	private Queue<ClientBanca> queue = new LinkedList<>();

	protected LocalDB() {
		thread = new Thread(this);
		thread.setName("SaveToLocalDB " + UID);
		thread.start();
		// thread.setDaemon(true);
	}

	public static LocalDB getInstance() {
		if (singleton == null) {
			singleton = new LocalDB();
		}
		return singleton;
	}

	@Override
	public void run() {
		while (true) {

			if (!queue.isEmpty()) {
				ClientBanca clientBanca = queue.poll();
				String fileStr = "db/".concat(Integer.toString(clientBanca.getCodPin())).concat(".ser");

				File file = new File(fileStr);
				if (file.exists()) {
					file.delete();
				}

				ObjectOutputStream oss = null;
				FileOutputStream fout = null;

				try {
					fout = new FileOutputStream(fileStr);
					oss = new ObjectOutputStream(fout);
					oss.writeObject(clientBanca);
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (oss != null) {
						try {
							oss.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}

			synchronized (this) {
				try {
					this.wait(WAKEUP);
				} catch (InterruptedException ex) {
					// System.out.println(ex);
				}
			}

		}
	}

	public void loadAllInfoFromDbLocal() {
		File folderDbLocal = new File("db");
		File[] listOfFiles = folderDbLocal.listFiles();
		System.out.println("Incarcare clienti existenti ...");
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {

				if (listOfFiles[i].getName().endsWith(".ser")) {
					ObjectInputStream objectinputstream = null;

					try {
						FileInputStream streamIn = new FileInputStream(listOfFiles[i]);
						objectinputstream = new ObjectInputStream(streamIn);
						ClientBanca clientBanca = (ClientBanca) objectinputstream.readObject();
						System.out
								.println("Client cu pin a fost identificat in baza de date " + listOfFiles[i].getName()
										+ " numele clientului este " + clientBanca.getClient().getNume() + ".");
						Main.LISTACLIENTBANCA.put(clientBanca.getCodPin(), clientBanca);

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (objectinputstream != null) {
							try {
								objectinputstream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}

				System.out.println("*********************************************************************");

			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Director " + listOfFiles[i].getName());
			}
		}

		System.out.println("Incarcare finalizata ...");

	}

	public Queue<ClientBanca> getQueue() {
		return queue;
	}

	public void deleteClientInfo(int pinIntrodus) {
		String fileName = "db/".concat(Integer.toString(pinIntrodus)).concat(".ser");
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
	}

	public void changePin(int pinOld, ClientBanca clientBanca) {
		
		deleteClientInfo(pinOld);
		queue.add(clientBanca);	
		
	}

}
