package ch.hearc.parapa_II;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import ch.hearc.parapa_II.labo.Database;
import ch.hearc.parapa_II.labo.Person;
import ch.hearc.parapa_II.labo.WaitingLogger;

public class Main {
	/**
	 * Start a new cancellable future task to run the console reading
	 * 
	 * @param args Program parameters, not used
	 */
	public static void main(String[] args) {
		new Thread(consoleTask).start();
	}

	/**
	 * Task starting the threads and reading the console, cancelled on EXIT or when
	 * all threads are done
	 */
	private static FutureTask<String> consoleTask = new FutureTask<>(new Callable<String>() {
		@Override
		public String call() throws Exception {
			int nbDocuments = 0;
			int nbPersons = 0;

			/*
			 * -----------------------------------------------------------------------------
			 * -----------
			 * DONE : Demander a l'utilisateur d'entrer un nombre de documents et un nombre
			 * de personne
			 * 
			 * Remarque : via console ou interface graphique
			 * -----------------------------------------------------------------------------
			 * -----------
			 */
			Scanner sc = new Scanner(System.in);

			do {
				System.out.print("Nombre de Personnes :");
				nbPersons = sc.nextInt();
			} while (nbPersons > 9 || nbPersons < 1);
			do {
				System.out.print("Nombre de Documents :");
				nbDocuments = sc.nextInt();
			} while (nbDocuments > 9 || nbDocuments < 1);

			// Database
			Database db = Database.getInstance();
			db.init(nbDocuments);

			// Waiting logger
			WaitingLogger waitingLogger = WaitingLogger.getInstance();

			// Create threads
			ArrayList<Person> persons = generatePopulation(db, nbPersons);

			// Start threads
			ArrayList<Thread> threads = new ArrayList<Thread>();
			for (Person person : persons) {
				Thread thread = new Thread(person);
				thread.setName(person.getName());
				thread.start();

				threads.add(thread);
			}

			// Setup waiting controller
			waitingLogger.assignConsoleFuture(consoleTask, persons);

			while (!consoleTask.isCancelled()) {

				System.out.println("Please press enter to continue or E/e to exit the program");
				String input = sc.nextLine();

				if (input.toLowerCase().equals("e") || waitingLogger.popNextLog()) {
					consoleTask.cancel(true);
				}
			}
			sc.close();

			threads//
					.stream()//
					.forEach(Thread::interrupt);

			System.out.println("Program terminated");
			return "";
		}
	});

	/**
	 * Generate a list of person and assign them a document from the database
	 * 
	 * @param db        Database containing all documents
	 * @param nbPersons Number of persons to generate
	 * @return a list of persons
	 */
	private static ArrayList<Person> generatePopulation(Database db, int nbPersons) {
		ArrayList<Person> persons = new ArrayList<Person>();

		long minStartingTime = 0;
		long maxStartingTime = 5000;
		long minDuration = 1000;
		long maxDuration = 5000;
		double probabilityReader = 0.5f;

		for (int i = 0; i < nbPersons; i++) {
			long startTime = (long) (minStartingTime + Math.random() * (maxStartingTime - minStartingTime));
			long duration = (long) (minDuration + Math.random() * (maxDuration - minDuration));
			Person.Role role = Math.random() < probabilityReader ? Person.Role.READER : Person.Role.WRITER;

			persons.add(new Person("Thread " + (i + 1), db.getRandomDocument(), role, roundTime(startTime),
					roundTime(duration)));
		}

		return persons;
	}

	/**
	 * Round milliseconds to 100
	 * 
	 * @param time Time to round
	 * @return rounded time
	 */
	private static long roundTime(long time) {
		return time - (time % 100);
	}

	static final boolean DEBUG = true;

}
