public class ReverseInteger {
	private int result;
	public ReverseInteger(int x)
	{
		this.result = reverse(x);
	}
	private int reverse(int x)
	{
		int tmp = x;
        int result = 0;
        while (tmp != 0) {
            int digit = tmp % 10;
            tmp /=10;
			try {
				result = Math.addExact(Math.multiplyExact(result, 10), digit);
			} catch (ArithmeticException e)
			{
				System.err.print(e.getStackTrace());
				System.out.print(e.getMessage());
				return 0;
			}
        }
        return result;
	}
	public int getResult()
	{
		return result;
	}
}
