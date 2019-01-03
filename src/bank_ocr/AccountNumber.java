package bank_ocr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class AccountNumber {

	private String number = "";
	
	private int index = 0;
	
	public AccountNumber(String num) {
		//num is string, we may use it to create actual form of accout number, and
		//using toDigital() method that form may be converted to digital form
		
		this.number = num;
	}
	
	public AccountNumber(String path, int index) {
		//need path to file with account numbers and position of account number to display
		this.index = index;
		int startIndex = index*4;
		int endIndex = startIndex+4;
		
		try {
				for (int j = startIndex; j < endIndex; j++) {
					this.number += Files.readAllLines(Paths.get(path)).get(j)+"\n";
				}
			
		} catch (IOException e) {
			System.out.println("File not found!");
        	System.exit(0);
		}
		

	}
		
	
	//Methods
	public void toDigital() {
		/*
		 * This method convert actual format of number to digital
		 */
		if(this.number.split("").length < 25) {
			String path = "C:\\bank_ocr_dojo_us1";
			
			String [] numberArray = this.number.split("");
			String digitalNumber = "";
			
			
				for (int l = 0; l < 4; l++) {
					
					for (int i = 0; i < numberArray.length; i++) {
							try {

									digitalNumber += Files.readAllLines(Paths.get(path))
											.get(Integer.parseInt(numberArray[i])*4+l).substring(0, 3);
								

							} catch (NumberFormatException e) {
								
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						
					}
					digitalNumber += "\n";
				}
				this.number = digitalNumber;
		}
		
		
	}

	public void toActual () {
		//convert digitalNumbers (that where digits are made using underscores and "|") 
		//to actual (normal/operational) form 
		
		//we may define each digit by hand, OR
		//enter path where all digits are written and write code which do this by us :-)
		if (this.number.split("").length > 25) {
			String path = "C:\\bank_ocr_dojo_us1";
			StringBuilder actualNumber = new StringBuilder("?????????");
			String digit;
			String pattern;
			
			for (int d = 0; d < 9; d++) {
				digit = "";
					
				 	for (int i = 0; i < number.length(); i+=28) {
						digit += number.substring((d*3)+i, (d*3)+3+i)+"\n";
					}
				 	int n = 0;
				 	for (int p = 0; p < 10; p++) {
				 		pattern = "";
				 		for (int lp = 0; lp < 4; lp ++) {
				 			try {
								pattern += Files.readAllLines(Paths.get(path)).get(n).substring(0, 3)+"\n";
								++n;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				 		}
				 		if (digit.equals(pattern)) {
				 			actualNumber.setCharAt(d, (char)(p+48));
				 		}
				 	}
			}
			
			this.number = actualNumber.toString().trim();
		}
	}

	public String getNumber() {
		return this.number;
	}
	
	public int getIndex() {
		return this.index;
	}

	public String checkSum() {

		
		String status = "";
		
		int checkSum = 0;
		
		String[] acNumberDigitsArray = getActual().split("");
		
		//checking if account number is legal/illegal
		
		if (getActual().contains("?")) {
			status = "ILL";
			return status;
		} else {
			//calculating checkSum
			for (int i = 0, j = 9; i < 9 && j > 0; i++, j--) {
				
				checkSum += Integer.parseInt(acNumberDigitsArray[i])*j;	
				
			}
		}

		if (checkSum % 11 == 0) {
			return status;
		} else {
			status = "ERR";
		}
		
		
		return status;
	}

	public String getActual() {
		/*
		 * Having digital fomrat of acc number method display created number in actual format
		 */
		String stringNumber = getNumber();
		if (this.number.split("").length > 25) {
			String path = "C:\\bank_ocr_dojo_us1";
			StringBuilder actualNumber = new StringBuilder("?????????");
			String digit;
			String pattern;
			
			for (int d = 0; d < 9; d++) {
				digit = "";
					
				 	for (int i = 0; i < number.length(); i+=28) {
						digit += number.substring((d*3)+i, (d*3)+3+i)+"\n";
					}
				 	int n = 0;
				 	for (int p = 0; p < 10; p++) {
				 		pattern = "";
				 		for (int lp = 0; lp < 4; lp ++) {
				 			try {
								pattern += Files.readAllLines(Paths.get(path)).get(n).substring(0, 3)+"\n";
								++n;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				 		}
				 		if (digit.equals(pattern)) {
				 			actualNumber.setCharAt(d, (char)(p+48));
				 		}
				 	}
			}
			
		stringNumber = actualNumber.toString().trim();
		}
		
		return stringNumber;
	}
	
	public String getActualNumberAndCheckSum () {
		
		return getActual()+" "+checkSum();
	}
	
	public int length() {
		int length = 0;
		length = getActual().length();
		
		return length;
	}

	public String getDigitalDigit(int index) {
		/*
		 * works only for 9 digit numbers!!
		 * that method allows to display any digit from 9 digit number that we specify as index
		 */
		String digit = "";
		
		toDigital();
		
		for (int i = 0; i < this.number.length(); i+=28) { // !!!! '28' works only for 9 digit numbers!!!!
			digit += this.number.substring((index*3)+0+i, (index*3)+3+i)+"\n";
		}
		
		
		return digit;
	}

	public String replaceDigit(int toChangeDigitIndex, int digit) {
		/*
		 * Used to display number after replacing specifieed digit as toChangeDigitIndex with digit
		 *
		 */
		String newDigit = new Integer(digit).toString();
		StringBuilder newNumber = new StringBuilder(getActual());
		
		newNumber.replace(toChangeDigitIndex, toChangeDigitIndex+1, newDigit);
		
		return newNumber.toString();
		
	}

	public void swapDigit(int toChangeDigitIndex, int digit) {
		/*
		 * works as replaceDigit but overrides number
		 */
		String newDigit = new Integer(digit).toString();
		StringBuilder newNumber = new StringBuilder(getActual());
		
		newNumber.replace(toChangeDigitIndex, toChangeDigitIndex+1, newDigit);
		
		this.number = newNumber.toString();
	}
}
