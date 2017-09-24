package club.nocs;

import java.io.File;
import java.util.Scanner;

import club.nocs.compiler.Compiler;
import club.nocs.parser.Parser;

public class Main
{
	public static void main(String[] args)
	{
		// compile the text based adventure
		Compiler c = new Compiler();
		File test = new File("text.tbaml");
		c.compile(test);
		
		// take input
		Scanner sc = new Scanner(System.in);
		Parser p = new Parser();
		
		while (true)
		{
			p.parse(sc.nextLine(), c.interpreter);
		}
	}
}
