package BankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Accounts {

	private Connection connection;
	private Scanner scanner;
	
	public Accounts(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public void open_account() {
		scanner.nextLine();
		System.out.print("Enter Full Name: ");
		String name = scanner.nextLine();
		System.out.print("Enter Emai: ");
		String email = scanner.nextLine();
		System.out.print("Enter Balance: ");
		float balance = scanner.nextFloat();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String pin = scanner.nextLine();
		long accNumber = generate_account_Number();
		
		
		String query = "insert into accounts(account_number, name, email, balance, security_pin) values (?, ?, ?, ?, ?);";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, accNumber);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, email);
			preparedStatement.setFloat(4, balance);
			preparedStatement.setString(5, pin);
			
			int rowsaffected = preparedStatement.executeUpdate();
			if(rowsaffected > 0) {
				System.out.println("Account created Susccesfully");
				System.out.println("Your Account Number: "+accNumber);
			}else {
				System.out.println("Account creation failed..");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public long generate_account_Number() {
		String query = "select account_number from accounts order by account_number desc limit 1";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			if(resultSet.next()) {
				long account_Number = resultSet.getLong("account_number");
				return account_Number + 1;
			}else {
				return 10000100;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return 10000100;
	}
	
	public boolean account_exits(String email) {
		String query = "select * from accounts where email = ?;";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	
	public void delete_account() {
		scanner.nextLine();
		System.out.print("Enter mail: ");
		String mail = scanner.nextLine();
		
		String query = "delete from accounts where email = ?;";
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			if(account_exits(mail)) {				
				preparedStatement.setString(1, mail);
				int rowsaffected = preparedStatement.executeUpdate();
				if(rowsaffected > 0) {
					System.out.println("Acccount deleted successfully.");
				}else {
					System.err.println("Account deletion failed.");
				}				
			}else {
				System.err.println("No account found");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void show_accounts() {
		String query = "Select * from accounts;";
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			System.out.println("+------------+------------+----------------------+---------+");
			
			while(resultSet.next()) {
				long account_number = resultSet.getLong("account_number");
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				int balance = resultSet.getInt("balance");
				
				System.out.printf("| %-10d | %-10s | %-20s | %-7d |\n",account_number, name, email, balance);
			}
			
			System.out.println("+------------+------------+----------------------+---------+");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
