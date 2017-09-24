package club.nocs.parser;

import java.util.Arrays;

import club.nocs.compiler.Interpreter;

public class Parser
{
	public void parse(String input, Interpreter interpreter)
	{
		input = input.trim();
		if (input.equals(""))
		{
			return;
		}
		// split input based on word spaces
		input.replaceAll("  ", " ");
		String[] words = input.split(" ");
		if (words.length == 0)
		{
			return;
		}
		
		// check for "the"
		int thePos = -1;
		for (int i = 0; i < words.length; i++)
		{
			if (words[i].equals("the"))
			{
				thePos = i;
			}
		}
		
		String verb = "";
		String noun = "";
		String prep = "";
		String adv = "";
		String adj = "";
		if (thePos < 0)
		{
			// if no "the", use "verb prep adj noun adv"
			// favor verb, noun, prep, adv, adj
			switch (words.length)
			{
				case 0:
					return;
				case 1:
					verb = words[0];
					break;
				case 2:
					verb = words[0];
					noun = words[1];
					break;
				case 3:
					verb = words[0];
					noun = words[2];
					prep = words[1];
					break;
				case 4:
					verb = words[0];
					noun = words[2];
					prep = words[1];
					adv = words[3];
					break;
				default:
					verb = words[0];
					noun = words[3];
					prep = words[1];
					adv = words[4];
					adj = words[2];
					break;
			}
		}
		// use "verb prep the adj noun adv"
		else
		{
			String[] beforeThe = Arrays.copyOfRange(words, 0, thePos);
			String[] afterThe = Arrays.copyOfRange(words, thePos + 1, words.length);
			switch (beforeThe.length)
			{
				case 0:
					return;
				case 1:
					verb = words[0];
					break;
				default:
					verb = words[0];
					prep = words[1];
					break;
			}
			switch (afterThe.length)
			{
				case 0:
					return;
				case 1:
					noun = words[0];
					break;
				case 2:
					noun = words[0];
					adv = words[1];
					break;
				default:
					noun = words[1];
					adv = words[2];
					adj = words[0];
					break;
			}
		}
		
		interpreter.input(verb, noun, prep, adv, adj);
	}
}
