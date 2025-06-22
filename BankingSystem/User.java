package BankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

	private Connection connection;
	private Scanner scanner;
	
	public User(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public String login() {
		scanner.nextLine();
		System.out.print("Enter Email: ");
		String email = scanner.nextLine();
		System.out.print("Enter Password: ");
		String password = scanner.nextLine();
		
		String query = "select * from user where email = ? and password =?;";
		
		try {
			PreparedStatement preapredStatement = connection.prepareStatement(query);
			preapredStatement.setString(1, email);
			preapredStatement.setString(2, password);
			
			ResultSet resultSet = preapredStatement.executeQuery();
			
			if(resultSet.next()) {
				return email;
			}else {
				return null;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public void registration() {
		scanner.nextLine();
		System.out.print("Enter full name: ");
		String name = scanner.nextLine();
		System.out.print("Enter Email: ");
		String email = scanner.nextLine();
		System.out.print("Enter Password: ");
		String password = scanner.nextLine();
		
		String query = "insert into user(name, email, password) values (?, ?, ?);";
		
		try {
			PreparedStatement preapredStatement = connection.prepareStatement(query);
			preapredStatement.setString(1, name);
			preapredStatement.setString(2, email);
			preapredStatement.setString(3, password);
			
			int rowsaffected = preapredStatement.executeUpdate();
			
			if(rowsaffected > 0) {
				System.out.println("Registration successfully.");
			}else {
				System.err.println("Registration failed.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public boolean exits(String email) {
		
		String query = "select * from user where email = ?;";
		try {
			PreparedStatement preapredStatement = connection.prepareStatement(query);
			preapredStatement.setString(1, email);
			
			ResultSet resultSet = preapredStatement.executeQuery();
			if(resultSet.next()) {
				return true;
			}else {
				return false;
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	
}
