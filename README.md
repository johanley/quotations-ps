Quotations collected over a lifetime of reading.
While reading a good book, I usually write down favourite passages.
I have several thousand collected in binders.
The quotes saved in this repository are some of my favourites.


# Data file: quotes.txt 
Structured data file, typed in manually.
Nested structures, with no repeated data. 
The source file uses 8859-1 encoding, because that encoding is especially friendly to PostScript.
The source file uses *ad hoc* symbols to denote the need for special processing of the text:
- *pre*(...) is meant to preserve white space (lines of poetry)
- *em*(...) is meant for emphasis/italics (not allowed in poetry, because my parsing code is mediocre)

Punctuation is a bit of a problem with this file:
 - the 8859-1 open-quote (octal 047) character is actually a back-quote on my PC's keyboard.
 - the 8859-1 encoding has a 'minus' character (octal 055) and a 'hyphen' (octal 255) character. 
   The hyphen is shorter, and usually is preferred.
   The hypen character has no dedicated key on my keyboard; Alt+0173 is needed.
   

# Generated data file: quotes_flat_file_ps.txt
Java code under `src` takes the source data file, 'flattens' it, and outputs one quote per line.
It also create a PostScript-friendly data structure (a dictionary) for each quote.


# Steps to create the output: book.pdf
- manual: update the quotes.txt file. Ensure the file is saved using the 8859-1 (ANSI) encoding. Be careful with punctuation.
- Java: run the `ParseAndGenerateFile` class. This generates `quotes_flat_file_ps.txt`.
- Ghostscript: run a Ghostscript command to generate a pdf. Example:

`C:\ghostscript\gs10.04.0\bin\gswin64c.exe -dNOSAFER -sDEVICE=pdfwrite -o book.pdf book.ps`

The cover is generated like so:

`C:\ghostscript\gs10.04.0\bin\gswin64c.exe -dNOSAFER -sDEVICE=pdfwrite -o cover.pdf cover.ps`

# PostScript 
The PostScript programming language is used to read the data files and create a pleasing PDF, suitable for printing.
The top-level PostScript file is `book.ps`. It uses other PostScript files.

Years ago, I used *iText* Java library for creating PDF files programmatically.
I now much prefer using the PostScript language for such tasks. 