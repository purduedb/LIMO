package example.edu.client.gui;


public class LimoPythonException extends Exception 
{
	private String out;
	private String err;
	private int lineCnt;
	
	public LimoPythonException()
	{
		super();
	}

    public LimoPythonException(Throwable e, String out, String err, int lineCnt)
    {
        super(e);
        this.out = out;
        this.err = err;
        this.lineCnt = lineCnt;
    }
    public String getMessage()
    {
    	String prefix = "  File \"<string>\", line ";
    	String newPrefix = "  Script area, line ";
    	String newPrefix2 = "  In the LIMO library file, line ";
    	String msg = super.getMessage();
    	String lineNumber = "";
    	String postfix = "";
    	String substring = "";
    	
    	String[] lines = msg.split("\n");
    	StringBuilder outMessage = new StringBuilder();
    	
    	
    	
    	for(int i = 0; i < lines.length; i++)
    	{
    		if(lines[i].startsWith(prefix))
    		{
    			System.out.println("line:");
    			System.out.println(lines[i]);
    			substring = lines[i].substring(prefix.length());
    			System.out.print(">");System.out.print(substring);System.out.print("<");
    			
//    			System.out.println(lineNumber);
				try
				{
					if(substring.contains(","))
					{
						lineNumber = substring.substring(0, substring.indexOf(","));
		    			postfix = substring.substring(substring.indexOf(","));
					}
					else if(substring.contains(" "))
					{
						lineNumber = substring.substring(0, substring.indexOf(" "));
		    			postfix = substring.substring(substring.indexOf(" "));
					}
					else
					{
						lineNumber = substring;
					}
	    			
	    			
					int intLineNumber = Integer.valueOf(lineNumber);
					int newNumber;
					
					if(intLineNumber >= lineCnt)
					{
						newNumber = intLineNumber - lineCnt;
						outMessage.append(newPrefix + newNumber + "\n");
					}
					else
					{
						newNumber = intLineNumber;
						outMessage.append(newPrefix2 + newNumber + postfix + "\n");
					}
					
					

					
//					outMessage.append(lines[i] + "\n");
					
				}
				catch (Exception e)
				{
					outMessage.append(lines[i] + "\n");
					continue;
				}
    		
    		}
    		else
    		{
    			outMessage.append(lines[i] + "\n");
    		}
    	}
    	
    	
        return out + "\n" + outMessage.toString();
    }
	

}
