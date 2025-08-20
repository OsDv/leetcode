public class StringToInteger {
	int result;
	public StringToInteger(String s)
	{
		result = myAtoi(s);
	}
	private int myAtoi(String s)
	{
		if (s.length()==0||s==null) return 0;
		int i=0;
		int res=0;
		boolean isNegative=false;
		boolean stop=false;
		boolean foundDigit = false;
		while(!stop&&i<s.length())
		{
			int c = s.charAt(i);
			if(c<='9'&&c>='0')
			{
				// res = res * 10+c+'0';
				foundDigit = true;
				try 
				{
					res = Math.addExact(Math.multiplyExact(res, 10), c-'0');
				} catch (ArithmeticException e)
				{
					System.out.println(e.getMessage());
					return (isNegative)? Integer.MIN_VALUE:Integer.MAX_VALUE;
				}
			} else if (!foundDigit)
			{
				switch (c) {
					case '-':
						isNegative = true;
						foundDigit = true;
						break;
					case '+':
						isNegative =false;
						foundDigit =true;
						break;
					case ' ':break;
					default:
						stop = true;
						break;
				}
			} else stop = true; //if ((c<='Z'&&c>='A')||(c<='z'&&c>='a'))
			i++;
		}
		return isNegative? -res:res;
	}

	public int getResult(){
		return  result;
	}
}
