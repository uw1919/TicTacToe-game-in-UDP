/*
 * Client.java
 * 
 * @version
 * $Id: Client.java, Version 1.0 12/08/2014 $
 * 
 * @revision
 * $Log initial version $
 * 
 */

//importing the required libraries
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * This class defines the client and sets up the connection with the server for
 * a game of Tic Tac Toe
 * 
 * @author Uday Vilas Wadhone
 * @author Harsh Patil
 *
 */
public class Client {
	// 3x3 arrat to initialise the board for the game
	static int[][] array = new int[3][3];

	/**
	 * Main method Sets up the connection with the server and update the game
	 * 
	 * @param args
	 *            no arguments
	 * @throws Exception
	 *             throws exception if connection is unsuccessful
	 */
	public static void main(String args[]) throws Exception {
		System.out.println("Client");
		// make an object of class Client
		Client client = new Client();
		// set up the board for the game
		client.setup();
		// print out the board
		client.print();
		// variable for keeping tracking of player's moves in the game
		int move;
		// socket to establish communication between server and client
		DatagramSocket socket;

		// while loop to send and receive messages between client and server
		// to keep the game going
		while (true) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			socket = new DatagramSocket();
			// initialise InetAddress with the localhost for a game on your
			// local
			// network
			InetAddress inetAddress = InetAddress.getByName("localhost");
			// stream to pass your move to the opponent
			byte[] sendstream = new byte[2];
			// stream to accept opponents move
			byte[] acceptstream = new byte[2];
			// variable to hold move made by opponent
			String position = br.readLine();
			// convert move made to integer type
			int clientmove = Integer.parseInt(position);
			// update the grid with the clients move
			client.grid1(clientmove);
			sendstream = position.getBytes();
			// create packet to send with the required data
			DatagramPacket sendpacket = new DatagramPacket(sendstream,
					sendstream.length, inetAddress, 8901);
			// send packet to server with the info about clients move
			socket.send(sendpacket);
			// create packet to receive data from server
			DatagramPacket receivepacket = new DatagramPacket(acceptstream,
					acceptstream.length);
			// receive packet from the server
			socket.receive(receivepacket);
			// get data from the packet
			String temp = new String(receivepacket.getData());
			// convert data to integer type
			int valid = Integer.parseInt(temp);
			// if valid==99, it means that the game has ended
			if (valid == 99) {

				break;
			}
			// convert move data to integer
			move = Integer.parseInt(temp);
			// update the grid according to the move made by server
			client.grid0(move);
		}
		socket.close();

	}

	/**
	 * Method to setup the board for the game
	 */
	public void setup() {
		for (int row = 0; row < 3; row++) {

			for (int col = 0; col < 3; col++) {
				// let initial values on board be 2
				array[row][col] = 2;

			}

		}

	}

	/**
	 * Method to print out the board
	 */
	public void print() {
		for (int row = 0; row < 3; row++) {

			System.out.print("| ");

			for (int col = 0; col < 3; col++) {

				System.out.print(array[row][col] + " | ");

			}

			System.out.println();

			System.out.println("-------------");

		}

	}

	/**
	 * Method to update clients move on the board
	 * 
	 * @param position
	 *            move made by the client
	 */
	public void grid1(int position) {

		// compare the input and value of that cell to update move made
		if (position == 00 && array[0][0] == 2) {
			array[0][0] = 1;
		}
		if (position == 01 && array[0][1] == 2) {
			array[0][1] = 1;
		}
		if (position == 02 && array[0][2] == 2) {
			array[0][2] = 1;
		}
		if (position == 10 && array[1][0] == 2) {
			array[1][0] = 1;
		}
		if (position == 11 && array[1][1] == 2) {
			array[1][1] = 1;
		}
		if (position == 12 && array[1][2] == 2) {
			array[1][2] = 1;
		}
		if (position == 20 && array[2][0] == 2) {
			array[2][0] = 1;
		}
		if (position == 21 && array[2][1] == 2) {
			array[2][1] = 1;
		}
		if (position == 22 && array[2][2] == 2) {
			array[2][2] = 1;
		}
		System.out.println();
		// print out the board with updated values
		print();
		System.out.println();
	}

	public void grid0(int position) {

		// compare the input and value of that cell to update move made
		if (position == 00 && array[0][0] == 2) {
			array[0][0] = 0;
		}
		if (position == 01 && array[0][1] == 2) {
			array[0][1] = 0;
		}
		if (position == 02 && array[0][2] == 2) {
			array[0][2] = 0;
		}
		if (position == 10 && array[1][0] == 2) {
			array[1][0] = 0;
		}
		if (position == 11 && array[1][1] == 2) {
			array[1][1] = 0;
		}
		if (position == 12 && array[1][2] == 2) {
			array[1][2] = 0;
		}
		if (position == 20 && array[2][0] == 2) {
			array[2][0] = 0;
		}
		if (position == 21 && array[2][1] == 2) {
			array[2][1] = 0;
		}
		if (position == 22 && array[2][2] == 2) {
			array[2][2] = 0;
		}
		System.out.println();
		// print out the board with updated values
		print();
		System.out.println();
	}

}