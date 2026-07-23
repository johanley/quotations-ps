**Generate a pleasing PDF from a text file containing quotations, collected over a lifetime of reading.**

While reading a good book, I usually write down favourite passages.
I have several thousand quotations collected in binders.
The quotes saved in this repository are some of my favourites.


# Data file: quotes.txt 
Structured data file, typed in manually.
Nested structures, with no repeated data. 
The source file uses `windows-1252` encoding (a single-byte encoding, which are friendly to PostScript).
The source file uses *ad hoc* symbols to denote the need for special processing of the text:
- *pre*(...) is meant to preserve white space (lines of poetry)
- *em*(...) is meant for emphasis/italics (not allowed in poetry, because my parsing code is mediocre)
   

# Generated data file: quotes_flat_file_ps.txt
Java code under `src` takes the source data file, 'flattens' it, and outputs one quote per line.
It also create a PostScript-friendly data structure for each quote (a dictionary).


# Steps to create the output: book.pdf
- manual: update the quotes.txt file. Ensure the file is saved using the `windows-1252` encoding.
- Java: run the `ParseAndGenerateFile` class. This generates `quotes_flat_file_ps.txt`.
- Ghostscript: run a Ghostscript command to generate a pdf. Example:

`C:\ghostscript\gs10.04.0\bin\gswin64c.exe -dNOSAFER -sDEVICE=pdfwrite -o BOOK.PDF BOOK.PS`

The cover is generated like so:

`C:\ghostscript\gs10.04.0\bin\gswin64c.exe -dNOSAFER -sDEVICE=pdfwrite -o BOOK-COVER.PDF BOOK-COVER.PS`

# PostScript 
The PostScript programming language is used to read the data files and create a pleasing PDF, suitable for printing.
(I'm using lulu.com for printing the book.)
The top-level PostScript file is `BOOK.PS`. It uses the other PostScript files in this project.

Years ago, I used the *iText* Java library for creating PDF files programmatically.
I now much prefer using the PostScript language for such tasks. 