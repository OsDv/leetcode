import java.io.*;
import java.util.Scanner;
class Main 
{
	public static void main(String[] args) {
		System.out.println(-2147483648/10);
		System.out.println(-2147483648%10);
		Scanner s= new Scanner(System.in);
		int x = s.nextInt();
		s.close();
		ReverseInteger reverse = new ReverseInteger(x);
		System.out.println(reverse.getResult());

	}
}
class LongestPalindromicSubString
{
	String s;
	String result;
	LongestPalindromicSubString(String s)
	{
		this.s = s;
		result = this.longestPalindrome();
	}

	private boolean isPalindromic(int start,int legnth)
	{
		int i = start;
		int j = start + legnth -1;
		while(i<j)
		{
			if (s.charAt(i)!=s.charAt(j)) return false;
			i++;
			j--;
		}
		return true;
	}
	private String longestPalindrome()
	{
		for (int i=s.length();i>0;i--)
		{
			for(int j=0;j<=s.length()-i;j++){
				System.out.println("i="+i+" ,j="+j);
				if (this.isPalindromic(j, i)) return s.substring(j, j+i);
			} 
		}
		return null;
	}
}