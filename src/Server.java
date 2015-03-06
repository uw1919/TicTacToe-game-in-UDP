/*
 * Server.java
 * 
 * @version
 * $Id: Server.java, Version 1.0 12/08/2014 $
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
 * This class sets up the server which can connect to client for a game of tic
 * tac toe.
 * 
 * @author Uday Vilas Wadhone
 * @author Harsh Patil
 *
 */
public class Server {
	// variable for tracking servers's moves
	static String position;
	// 3x3 array to set up board for the game
	static int[][] array = new int[3][3];

	/**
	 * Main method Sets up necessary pre requisites for a network connection
	 * with the client
	 * 
	 * @param args
	 *            no arguments
	 * @throws Exception
	 *             throws exception if connection is unsuccessful
	 */
	public static void main(String args[]) throws Exception {
		System.out.println("Server");
		// declaring datagram type socket for communication
		DatagramSocket serversocket = new DatagramSocket(8901);
		// array to take in input from the client
		byte[] acceptstream = new byte[2];
		// array to send out output to client
		byte[] sendstream = new byte[2];
		// variable to record move of the server
		int move;
		// create an object of Server class
		Server server = new Server();
		// set up the board for the game
		server.setup();
		// print out the board
		server.print();

		// while loop to listen to server and receive the packets that clients
		// sends and update the game accordingly
		while (true) {
			// declaring variable to take in input from client
			DatagramPacket receivepacket = new DatagramPacket(acceptstream,
					acceptstream.length);
			// receive the packet sent by client
			serversocket.receive(receivepacket);
			// record position that the client has moved to
			position = new String(receivepacket.getData());
			// convert the position to number
			move = Integer.parseInt(position);
			// update the board accordingly
			server.board(move);
			// variable to check if someone has won
			int check1 = server.lookforwin1();
			// variable for port and InetAddress required to send packets
			InetAddress inetAddress = receivepacket.getAddress();
			int PORT = receivepacket.getPort();
			// if client has won send the message through sendpacket variable
			if (check1 == 1) {
				String clnt = "99";
				sendstream = clnt.getBytes();
				// make packet that is to be sent with the required data
				DatagramPacket sendpacket = new DatagramPacket(sendstream,
						sendstream.length, inetAddress, PORT);
				// send packet to client
				serversocket.send(sendpacket);
				// output that client has won
				System.out.println("Client Wins!!!");
				break;
			}
			// check if the client and server have tied the game
			int result = server.tied();
			// if game is tied, notify client of it
			if (result == 1) {
				String client = "99";
				sendstream = client.getBytes();
				System.out.println("Tie");
				sendstream = client.getBytes();
				// make packet with required data
				DatagramPacket sendpacket = new DatagramPacket(sendstream,
						sendstream.length, inetAddress, PORT);
				// send packet to client
				serversocket.send(sendpacket);
				break;
			}
			// variable to read line in the input stream
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			// read the move initiated
			String servermove = br.readLine();
			// convert the data to integer type
			int serverposition = Integer.parseInt(servermove);
			// update the board with server position
			server.serverboard(serverposition);
			// check is server has won
			int check2 = server.lookforwin0();
			// if server has won, notify it of the same by sending a packet
			if (check2 == 1) {
				String client = "99";
				sendstream = client.getBytes();
				System.out.println("Server WINS");
				sendstream = client.getBytes();
				DatagramPacket sendpacket = new DatagramPacket(sendstream,
						sendstream.length, inetAddress, PORT);
				serversocket.send(sendpacket);
				break;
			}
			// send servers move to client
			sendstream = servermove.getBytes();
			// make packet with data about servers move
			DatagramPacket sendpacket = new DatagramPacket(sendstream,
					sendstream.length, inetAddress, PORT);
			// send the packet
			serversocket.send(sendpacket);

		}
		serversocket.close();
	}

	/**
	 * Set up the board by initialising its values
	 */
	public void setup() {
		for (int row = 0; row < 3; row++) {

			for (int col = 0; col < 3; col++) {
				// let initial blank values be integer = 2
				array[row][col] = 2;

			}

		}

	}

	/**
	 * Check if the game is tied between the server and client
	 * 
	 * @return int 0 if it not tied, 1 if it is tied
	 */
	public int tied() {
		// variable to keep track if all cells are filled up
		int counter = 0;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if (array[row][col] != 2) {
					// increment counter if a move has been made in that cell
					counter++;
				}
			}
		}
		// condition to check if board is full
		if (counter == 9) {
			return 1;
		}
		return 0;

	}

	/**
	 * Method to check if client has won.
	 * 
	 * @return int returns 1 if client has won
	 */
	public int lookforwin1() {
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				// check if there are same integers in vertical setup
				if (array[row][col] == 1) {
					if ((row + 1) < 3 && (row + 2) < 3) {
						if (array[row + 1][col] == 1
								&& array[row + 2][col] == 1) {

							return 1;
						}
					}

				}
				// check if client is winning diagonally
				if (array[row][col] == 1) {
					if ((row + 1) < 3 && (row + 2) < 3 && (col + 1) < 3
							&& (col + 2) < 3) {
						if (array[row + 1][col + 1] == 1
								&& array[row + 2][col + 2] == 1) {

							return 1;
						}
					}

				}

				if (array[row][col] == 1) {
					if ((row + 1) < 3 && (col - 1) > -1 && (col - 2) > -1
							&& (row + 2) < 3) {
						if (array[row + 1][col - 1] == 1
								&& array[row + 2][col - 2] == 1) {

							return 1;
						}
					}

				}
				// check if client is winning horizontally
				if (array[row][col] == 1) {
					if ((col + 1) < 3 && (col + 2) < 3) {
						if (array[row][col + 1] == 1
								&& array[row][col + 2] == 1) {

							return 1;
						}
					}

				}

			}

		}
		return 0;

	}

	/**
	 * Check if server has won the game
	 * 
	 * @return int returns 1 if server has won the game
	 */
	public int lookforwin0() {
		for (int row = 0; row < 3; row++) {

			for (int col = 0; col < 3; col++) {
				// Check if server has won in the vertical setup
				if (array[row][col] == 0) {
					if ((row + 1) < 3 && (row + 2) < 3) {
						if (array[row + 1][col] == 0
								&& array[row + 2][col] == 0) {

							return 1;
						}
					}

				}
				// check if server is winning in the diagonal setup
				if (array[row][col] == 0) {
					if ((row + 1) < 3 && (row + 2) < 3 && (col + 1) < 3
							&& (col + 2) < 3) {
						if (array[row + 1][col + 1] == 0
								&& array[row + 2][col + 2] == 0) {

							return 1;
						}
					}

				}

				if (array[row][col] == 0) {
					if ((row + 1) < 3 && (col - 1) > -1 && (col - 2) > -1
							&& (row + 2) < 3) {
						if (array[row + 1][col - 1] == 0
								&& array[row + 2][col - 2] == 0) {

							return 1;
						}
					}

				}
				// check if server is winning in the horizontal setup
				if (array[row][col] == 0) {
					if ((col + 1) < 3 && (col + 2) < 3) {
						if (array[row][col + 1] == 0
								&& array[row][col + 2] == 0) {
							return 1;
						}
					}
				}
			}
		}
		return 0;

	}

	/**
	 * Method to print out the updated to server and client everytime a move is
	 * made
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
	 * Method to interpret which position the player has updated
	 * 
	 * @param position
	 *            pass the position entered by the player
	 */
	public void board(int position) {

		// checks the co-ordinates provided by player and updates the board
		// accordingly
		if (position == 00 && array[0][0] == 2) {
			System.out.println("in 00 loop");
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
		// print out the board after updating the move made
		print();
		System.out.println();
	}

	/**
	 * Update the board on server side
	 * 
	 * @param position
	 */
	public void serverboard(int position) {

		// checks the co-ordinates provided by player and updates the board
		// accordingly
		if (position == 00 && array[0][0] == 2) {
			System.out.println("in 00 loop");
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
		// print out the board after updating the move made
		print();
		System.out.println();
	}

}
