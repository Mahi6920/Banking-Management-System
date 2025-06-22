package BankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Admin {

	private Connection connection;
	private Scanner scanner;

	public Admin(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public String login() {
		scanner.nextLine();
		System.out.print("Enter Email: ");
		String email = scanner.nextLine();
		System.out.print("Enter Password: ");
		String password = scanner.nextLine();
		
		String query = "select * from admin where email = ? and adminPassword = ?;";
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			
			ResultSet resultSet=  preparedStatement.executeQuery();
			
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
		System.out.println("Register Here...");
		scanner.nextLine();
		System.out.print("Enter Full Name: ");
		String name = scanner.nextLine();
		System.out.print("Enter Password: ");
		String password = scanner.nextLine();
		System.out.print("Enter Email: ");
		String email = scanner.nextLine();

		// Registration Query
		String query = "insert into admin(adminName, adminPassword, email) values (?, ?, ?);";
		if(exist(email)) {
			System.out.println("This email already exits..");
			return;

		}else {
			try {
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, name);
				preparedStatement.setString(2, password);
				preparedStatement.setString(3, email);
				
				int rowsaffected = preparedStatement.executeUpdate();
				
				if(rowsaffected > 0) {
					System.out.println("Registration succesfully..");
				}else {
					System.err.println("Registration failed..");
				}

			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	public boolean exist(String email) {
		String query = "select * from admin where email = ?;";
		
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

}
