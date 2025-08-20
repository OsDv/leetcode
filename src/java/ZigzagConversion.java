public class ZigzagConversion {
	String result;
	int numRows;

	// Constructor
	public ZigzagConversion(String s,int numRows)
	{
		this.numRows = numRows;
		result = convert(s);
	}
	private String convert(String s)
	{
		if(numRows==1) return new String(s);
		char[] res = new char[s.length()];
		int index=0;// used to fill array
		// fill first row
		int i=0;
		while(i<s.length())
		{
			res[index++] = s.charAt(i);
			i += (numRows-1)*2;
		}
		// fill inner rows
		for (int j=1;j<this.numRows-1;j++)
		{
			boolean up = false;
			i=j;
			while(i<s.length())
			{
				res[index++] = s.charAt(i);
				if (up){
					i += 2*j;
				} else {
					i += (numRows-j-1)*2;
				}
				up = !up;
			}
		}
		// fill last row
		i = numRows -1;
		while(i<s.length())
		{
			res[index++] = s.charAt(i);
			i += (numRows-1)*2;
		}
		return new String(res);
	}

	public String getResutl()
	{
		return this.result;
	}
}
