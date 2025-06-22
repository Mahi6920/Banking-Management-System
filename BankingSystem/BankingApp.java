package BankingSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {

	private static final String url = "jdbc:mysql://localhost:3306/banking_system";
	
	private static final String userName = "root";
	
	private static final String password = "mahi";
	
	public static void main(String[] args) {
		
		
		
		try {
			Connection connection = DriverManager.getConnection(url, userName, password);
			Scanner scanner = new Scanner(System.in);
			
			Admin admin = new Admin(connection, scanner);
			User user = new User(connection, scanner);
			Accounts accounts = new Accounts(connection ,scanner);
			AccountManger accountManger = new AccountManger(connection, scanner);
			Transaction transaction = new Transaction(connection);
			
			System.out.println("1.Admin");
			System.out.println("2.User");
			System.out.print("Enter option: ");
			int option = scanner.nextInt();
			
			switch(option) {
			case 1://admin
				System.out.println("1.Login");
				System.out.println("2.Registration");
				System.out.print("Enter your option: ");
				int adminOption = scanner.nextInt();
				while(true) {
					switch(adminOption){
					case 1:
						String email;
						email = admin.login();
						if(admin.exist(email)) {
							System.out.println("Login..");
							
							System.out.println("1.Account Creation");
							System.out.println("2.Delete Account");
							System.out.println("3.Show Accounts");
							System.out.print("Enter your option: ");
							int accOption = scanner.nextInt();
							
							switch(accOption) {
							case 1:
								accounts.open_account();
								break;
								
							case 2:
								accounts.delete_account();
								break;
								
							case 3:
								accounts.show_accounts(); 
								break;
								
							default :
								System.err.println("Invalid option..");
								break;
							}				
							break;
							
						}else if(email == null) {
							System.out.println("No email");
							admin.registration();
						}else {
							System.out.println("Error");
							admin.registration();
						}
						break;
						
					case 2:
						 admin.registration();
						break;
						
					default :
						System.out.println("Invalid option");
						break;
					}
				}
				
//				break;
				
			case 2://user
				System.out.println("1.Login");
				System.out.println("2.Registration");
				System.out.print("Enter option: ");
				int userOption = scanner.nextInt();
				
				switch(userOption) {
				case 1:
					String email1 = user.login();
					if(user.exits(email1)) {
						while(true) {
							System.out.println("Login..");
							
							System.out.println("1.Deposit");
							System.out.println("2.Withdraw");
							System.out.println("3.Transfer");
							System.out.println("4.Get Balance");
							System.out.println("5.History");
							System.out.print("Enter your option: ");
							int balOption = scanner.nextInt();
							
							switch(balOption) {
							case 1:
								accountManger.deposit(email1);
								break;
								
							case 2:
								accountManger.withdraw(email1);
								break;
								
							case 3:
								accountManger.transfer(email1);
								break;
								
							case 4:
								accountManger.get_Balance(email1);
								break;
								
							case 5:
								transaction.showTransactions(accountManger.get_acc_No(email1));
								break;
								
							default :
								System.err.println("Invalid option.");
								break;
							}
						}
//						break;
					}else if(email1 == null) {
						System.out.println("No email");
						user.registration();
					}else {
						System.err.println("Error");
						break;
					}
					break;
					
				case 2:
					user.registration();
					break;
					
				default:
					System.err.println("Invalid option.");
					break;
				}
				
				break;
				
			default :
				System.out.println("Invalid option.");
				break;
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
}
