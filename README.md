Quotations collected over a lifetime of reading.
I write quotations down while reading a good book.
I have several thousand collected in binders.
The quotes saved in this repo are some of my favourites.

# Data file: quotes.txt 
Structured data file, typed in manually.
No repeated data. 
The source file uses 8859-1 encoding, because that encoding is especially friendly to PostScript.
The source file uses *ad hoc* symbols to denote the need for special processing of the text:
- *pre*(...) is meant to preserve white space (lines of poetry)
- *em*(...) is meant for emphasis/italics (not allowed in poetry, because my parsing code is mediocre)

# Generated data file: quotes_flat_file_ps.txt
The Java code in `src` takes the source data file, 'flattens' it, having one quote per line.
It also create a PostScript-friendly data structure (a dictionary) for each quote.

# Steps to create the output: book.pdf
- manual: update the quotes.txt file. Ensure the file is saved using the 8859-1 (ANSI) encoding.
- Java: run the `ParseAndGenerateFile` class. This generates `quotes_flat_file_ps.txt`.
- Ghostscript: run a Ghostscript command to generate a pdf. Example:

`C:\ghostscript\gs10.04.0\bin\gswin64c.exe -dNOSAFER -sDEVICE=pdfwrite -o book.pdf book.ps`

# PostScript 
The PostScript programming language is used to read the data files and create a pleasing PDF, suitable for printing.
The top-level PostScript file is `book.ps`.
I used to use the iText Java library for that task. 
I now much prefer PostScript for such tasks. 