import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class GenerateClients {
	
	public static String[] generateClientInsertStatements(int size) {
		
		String[] clients = new String[size];
		
		String[] givenNames = {
				"Noah","Liam","Jackson","Lucas","Logan","Benjamin","Jacob","William","Oliver","James",
				"Olivia","Emma","Charlotte","Sophia","Aria","Ava","Chloe","Zoey","Abigail","Amilia"
		};
		
		String[] familyNames = {
				"Smith","Brown","Tremblay","Martin","Roy",
				"Wilson","Macdonald","Gagnon","Johnson","Taylor"
		};
		
		String[] streets = {
				"rue Sherbrooke", "rue Parc","rue Laurier","blvd St-Joseph","rue Rachel"
		};
		
		ArrayList<ArrayList<Integer>> streetNumbers = new ArrayList<ArrayList<Integer>>();
		for(int j=0;j<streets.length;j++) {
			streetNumbers.add(new ArrayList<Integer>());
		}
		
		//phone numbers
		ArrayList<Integer> firstNumbers = new ArrayList<Integer>();
		ArrayList<Integer> secondNumbers = new ArrayList<Integer>();
		ArrayList<BigInteger> phoneNumbers = new ArrayList<BigInteger>();
		
		Random random = new Random();
		int index = 0;
		Integer number = 0;
		Integer streetNumber = 0;
		Integer firstNumber = 0;
		Integer secondNumber = 0;
		
		for(int i=0;i<size;i++) {
			//choose random givenName
			index = random.nextInt(givenNames.length);
			String firstName = givenNames[index];
			
			//choose random familyName
			index = random.nextInt(familyNames.length);
			String lastName = familyNames[index];
			String fullName = firstName + " " + lastName;
			//System.out.println("Name: "+ fullName);
			
			//calculate address
			index = random.nextInt(streets.length);
			String street = streets[index];
			streetNumber = random.nextInt(100);
			while(streetNumbers.get(index).contains(streetNumber)) {
				//generate unique number
				streetNumber = random.nextInt(100);
			}
			streetNumbers.get(index).add(streetNumber);
			String fullAddress = streetNumber.toString() + " " + street;
			//System.out.println("Address: "+ fullAddress);
			
			//calculate phone number -> add one to phone number
			//number = (514) + 000 000i
			firstNumber = random.nextInt(1000);
			secondNumber = random.nextInt(10000);
			String phoneNumber = "514"+firstNumber.toString()+secondNumber.toString();
			BigInteger phone = new BigInteger(phoneNumber);
			
			while(phoneNumbers.contains(phone)) {
				//generate unique number
				firstNumber = random.nextInt(1000);
				secondNumber = random.nextInt(10000);
				phoneNumber = "514"+firstNumber.toString()+secondNumber.toString();
				phone = new BigInteger(phoneNumber);
			}
			
			phoneNumbers.add(phone);
			
			//System.out.println("Phone Number: " + phone.toString());
			
			//Create statement and add to array
			clients[i] = InitializeReset.getInsertClient(fullName, phoneNumber, fullAddress);
			//System.out.println("Insert Client: "+clients[i]);
		}
		
		return clients;
	}

}
