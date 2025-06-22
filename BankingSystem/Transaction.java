package BankingSystem;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transaction {

	private Connection connection;
	
	public Transaction(Connection connection) {
		this.connection = connection;
	}
	
	public void transcations(long account_number, String transaction_type, float amount) {
		String query = "insert into transaction(account_number, transaction_type, amount, transaction_date) values (?, ?, ?, now());";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, account_number);
			preparedStatement.setString(2, transaction_type);
			preparedStatement.setFloat(3, amount);
			
			int rowsaffected = preparedStatement.executeUpdate();
			
			if(rowsaffected > 0) {
				System.out.println("Updated succesfully");
			}else {
				System.out.println("Updation failed");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void showTransactions(long account_number) {
		String query = "select * from transaction where account_number = ?;";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, account_number);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("+-------+------------+------------+------------+-----------+");
			while(resultSet.next()) {
				int id = resultSet.getInt("transaction_id");
				long account_number_get = resultSet.getLong("account_number");
				String transcation_type = resultSet.getString("transaction_type");
				int amount = resultSet.getInt("amount");
				java.sql.Timestamp date = resultSet.getTimestamp("transaction_date"); 
				
				System.out.printf("| %-5d | %-10d | %-10s | %-9d  | %-9td |\n",id, account_number_get, transcation_type, amount, date);
			}
			System.out.println("+-------+------------+------------+------------+-----------+");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
