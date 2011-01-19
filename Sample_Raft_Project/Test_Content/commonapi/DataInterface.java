package commonapi;

import java.util.ArrayList;
import java.util.Random;

public class DataInterface {
	
	public String Create_Random_Message(int MaxLength) throws Exception{

	    ArrayList<Character> al=new ArrayList<Character>();

	        al.add((char)(' '));

	        for(int i=0;i<26;i++){

	           al.add((char)('a'+(int)i));

	           al.add((char)('A'+(int)i));
	        }

	        al.add((char)(' '));

	        for(int i=0;i<10;i++){

	           al.add((char)(i+48));
	        }

	        al.add((char)(' '));

	        StringBuilder sb =new StringBuilder();

	        for(int i=0;i<MaxLength;i++) {

	             Random random = new Random();

	             int max=al.size();

	             int s = random.nextInt(max)%(max-0+1) + 0;

	             String value=String.valueOf(al.get(s));

	             sb.append(value);
	         }
//	       System.out.println(sb.toString());
//	       System.out.println(sb.length());
	      return sb.toString();

	 }

}




