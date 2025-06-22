package BankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManger {

	private Connection connection;
	private Scanner scanner;

	public AccountManger(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void deposit(String mail) throws SQLException {
		
		Transaction t = new Transaction(connection);
		String deposit = "deposit";
		
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		float deposit_amount = scanner.nextFloat();
		scanner.nextLine();
		System.out.print("Enter pin: ");
		String pin = scanner.nextLine();

		String query = "select * from accounts where security_pin = ?;";

		try {
			connection.setAutoCommit(false);
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, pin);
			ResultSet resultSet = preparedStatement.executeQuery();

			if(resultSet.next()) {
				if(deposit_amount > 0) {
					try {
						String querydeposit = "update accounts set balance = balance + ? where email = ? and security_pin = ?;"; 
						PreparedStatement preparedStatement1 = connection.prepareStatement(querydeposit);
						preparedStatement1.setFloat(1, deposit_amount);
						preparedStatement1.setString(2, mail);
						preparedStatement1.setString(3, pin);

						int rowsaffected = preparedStatement1.executeUpdate();

						if(rowsaffected > 0) {
							System.out.println("Amount deposited successfully");
							connection.commit();
							connection.setAutoCommit(true);
							// To add transaction into transaction table 
							t.transcations(get_acc_No(mail), deposit, deposit_amount);
						}else {
							System.out.println("Amount deposition failed");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} catch (SQLException e) {
						System.out.println(e.getMessage());
					}
				}else {
					System.out.println("Negative Amount Entered");
				}
			}else {
				System.err.println("Invalid pin");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		connection.setAutoCommit(true);
	}

	public void withdraw(String mail) throws SQLException {
		
		Transaction t = new Transaction(connection);
		String withdraw = "withdraw";
		
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		float balance = scanner.nextFloat();
		scanner.nextLine();
		System.out.print("Enter pin: ");
		String pin = scanner.nextLine();

		String query = "select * from accounts where security_pin = ?;";

		try {
			connection.setAutoCommit(false);
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, pin);

			ResultSet resultSet = preparedStatement.executeQuery();

			if(resultSet.next()) {
				if(balance > 0) {
					String query2 = "update accounts set balance = balance - ? where email = ? and security_pin = ?;";
					try {
						PreparedStatement preparedStatement1 = connection.prepareStatement(query2);
						preparedStatement1.setFloat(1, balance);
						preparedStatement1.setString(2, mail);
						preparedStatement1.setString(3, pin);

						int rowsaffected = preparedStatement1.executeUpdate();

						if(rowsaffected > 0) {
							System.out.println("Withdraw Successfully");
							connection.commit();
							connection.setAutoCommit(true);
							// To add transaction into transaction table 
							t.transcations(get_acc_No(mail), withdraw, balance);
						}else {
							System.out.println("Withdraw Failed");
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} catch (SQLException e) {
						System.out.println(e.getMessage());

					}

				}else {
					System.out.println("Negative amount entered");
				}
			}else {
				System.err.println("Invalid pin");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		connection.setAutoCommit(true);
	}

	public void get_Balance(String email) {
		String query = "select balance from accounts where email = ?;";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);

			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				float balance = resultSet.getFloat("balance");
				System.out.println("Account Balance: "+balance);
			}else {
				System.err.println("No Account Found");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void transfer(String mail) throws SQLException {
		
		Transaction t = new Transaction(connection);
		String deposit = "deposit";
		String withdraw = "withdraw";
		
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		float balance = scanner.nextLong();
		System.out.print("Enter Account Number: ");		
		long depositAccNumber = scanner.nextLong();
		scanner.nextLine();
		System.out.print("Enter pin: ");
		String pin = scanner.nextLine();
		
		String depositQuery = "update accounts set balance = balance + ? where account_Number = ?";
		String creditQuery = "update accounts set balance = balance - ? where email = ? and security_pin = ?;";
		
		try {
			connection.setAutoCommit(false);
			PreparedStatement preparedstatementcredit = connection.prepareStatement(creditQuery);
			PreparedStatement preparedstatementdeposit = connection.prepareStatement(depositQuery);
			
			preparedstatementcredit.setFloat(1, balance);
			preparedstatementcredit.setString(2, mail);
			preparedstatementcredit.setString(3, pin);
			
			preparedstatementdeposit.setFloat(1, balance);
			preparedstatementdeposit.setLong(2, depositAccNumber);
			
			int rowsaffected1 = preparedstatementcredit.executeUpdate();
			int rowsaffected2 = preparedstatementdeposit.executeUpdate();
			
			if(rowsaffected1 > 0 && rowsaffected2 > 0) {
				connection.commit();
				connection.setAutoCommit(true);
				System.out.println("Transaction successful");
				
				t.transcations(get_acc_No(mail), withdraw, balance);
				t.transcations(depositAccNumber, deposit, balance);
			}else {
				connection.rollback();
				connection.setAutoCommit(true);
				System.out.println("Transaction failed");
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		connection.setAutoCommit(true);
	}
	
	public long get_acc_No(String mail) {
		String query = "select account_number from accounts where email = ?;";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, mail);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				long account_number = resultSet.getLong("account_number");
				return account_number;
			}else {
				return 0;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return 0;
	}
}
