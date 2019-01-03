package bank_ocr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class BankOCR {

	public static void main(String[] args) {

		
		String path = "C:\\bank_ocr_dojo_us3";		
		
		findNumbers(path);
		
	}
	
	
	public static String defineNumbers (int number) {
		
		String path = "C:\\bank_ocr_dojo_us1";
		
		List<String> readNumbers = new ArrayList<>();
		
		try {
			List<String> accNumbers = Files.readAllLines(Paths.get(path));
			
			String helper;
			int n = 0;
			for(int i = 0; i < 10; i++) {
				helper = "";
				for (int j = 0; j < 4; j++) {

					helper += accNumbers.get(n).substring(0, 3)+"\n";
					++n;
					
				}
				readNumbers.add(i, helper);
			}
			//System.out.println(readNumbers.get(9));
			
		} catch (IOException e) {
			System.out.println("File not found!");
        	System.exit(0);;
		}
		
		return readNumbers.get(number);
		
	}

	public static long accNumbersCounter (String path) {

		
		long numberOfLines = 0;
		try {
			numberOfLines = Files.lines(Paths.get(path)).count();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		long numberOfAccNumbers = numberOfLines/4;
		
		return numberOfAccNumbers;
	}

	public static void findNumbers (String path) {
		List<String> ambList = new ArrayList<>();
		int numberOfAccounts = (int)accNumbersCounter(path);
		for (int n = 0; n < numberOfAccounts; n++) {
			AccountNumber number = new AccountNumber(path, n);
			ambList.clear();
			System.out.print(number.getNumber());
	
			if(number.getActual().contains("?")) {
				Pattern findQuestionMark = Pattern.compile("([?])"); 
				Matcher matcher = findQuestionMark.matcher(number.getActual());
				int count = 0;
				while (matcher.find()) 
					count++;
				if(count > 1) {
					ambList.add(number.getActualNumberAndCheckSum());
				} else {
					
					ambList.addAll((findNumbersList(number.getActual().indexOf("?"), number)));
				}
	
			}
			
			if(number.checkSum().equals("ERR")) {
				for(int numDigitIndex = 0; numDigitIndex < number.length(); numDigitIndex++) {
					ambList.addAll(findNumbersList(numDigitIndex,number));
				}
			}
			
			if(number.checkSum().equals("")) {
				ambList.add(number.getActual());
			}
			if(ambList.size() > 1) {
				System.out.println(number.getActual()+" AMB " + ambList);
			} else {
				for(String s:ambList) {
					System.out.println(s);
				}
			}
		}
		
		
		
	}

	public static List<String> findNumbersList (int numDigitIndex, AccountNumber number) {
		
		List<String> newNumbers = new ArrayList<>();
		String digit = number.getDigitalDigit(numDigitIndex);
		for (int pipePlace = 0; pipePlace < digit.length(); pipePlace ++) {
			if (digit.substring(pipePlace, pipePlace+1).equals(" ")) {
				
				if(pipePlace % 2 == 0 && pipePlace != 0 && pipePlace != 2 && pipePlace < 12) {
					
					String temporaryDigit = "";
					temporaryDigit = digit.substring(0, pipePlace)+"|"+digit.substring(pipePlace+1, digit.length());
					
					for (int d = 0; d < 10; d ++) {
						if (temporaryDigit.equals(defineNumbers(d))) {
							AccountNumber helper = new AccountNumber(number.replaceDigit(numDigitIndex, d));
							if(!helper.checkSum().equals("ERR")) {
								newNumbers.add(helper.getActual());
							}  
						}
					}
				}
				if(pipePlace % 2 != 0 && pipePlace < 12) {
					
					String temporaryDigit = "";
					temporaryDigit = digit.substring(0, pipePlace)+"_"+digit.substring(pipePlace+1, digit.length());
					
					for (int d = 0; d < 10; d ++) {
						if (temporaryDigit.equals(defineNumbers(d))) {
							AccountNumber helper = new AccountNumber(number.replaceDigit(numDigitIndex, d));
							
							if(!helper.checkSum().equals("ERR")) {
								newNumbers.add(helper.getActual());
							}
						}
					}
				}

			}
		}
		for (int pipePlace = 0; pipePlace < digit.length(); pipePlace ++) {
			if (digit.substring(pipePlace, pipePlace+1).equals("_") || digit.substring(pipePlace, pipePlace+1).equals("|")) {
				
				String temporaryDigit = "";
				temporaryDigit = digit.substring(0, pipePlace)+" "+digit.substring(pipePlace+1, digit.length());
				
				for (int d = 0; d < 10; d ++) {
					if (temporaryDigit.equals(defineNumbers(d))) {
						AccountNumber helper = new AccountNumber(number.replaceDigit(numDigitIndex, d));
						
						if(!helper.checkSum().equals("ERR")) {
							newNumbers.add(helper.getActual());
						} 
					}
				}
			}
		}
		if(newNumbers.isEmpty() && !number.checkSum().equals("ERR"))
			newNumbers.add(number.getActualNumberAndCheckSum());
		
		return newNumbers;
	}
}
