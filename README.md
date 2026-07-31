**Generate a pleasing PDF from a text file containing quotations, collected over a lifetime of reading.**

While reading a good book, I usually write down favourite passages.
I have several thousand quotations collected in binders.
The quotes saved in this repository are some of my favourites.


# Input File
The core data file is typed in manually: 

`quotes-data-1252\quotes_flat_file_ps.txt`

Each line is a PostScript dictionary, containing the data related to a single quote.

This file uses the `windows-1252` encoding.
This is a single-byte encoding. 
Single-byte encodings are friendly to PostScript.

Cons: 
- repetition:  for each quotation, it repeats the author's name and the title of the work.
- you have to follow the rules of PostScript syntax.
 
Pros: 
- it can be consumed by PostScript as is, with no parsing.
- if I need to implement to kinds of formatting, it can be done directly with normal PostScript.
- it allows for automated generation of an index.
- the order of items here is the order of items in the output.
   

# Generate The Output File
- update the `quotes_flat_file_ps.txt ` file. 
- use the curly quotes available with the `windows-1252` encoding.
- ensure the file is saved using the `windows-1252` encoding.
- Ghostscript: run a Ghostscript command to generate a pdf. See BUILD.BAT for an example.

`C:\ghostscript\gs10.04.0\bin\gswin64c.exe -dNOSAFER -sDEVICE=pdfwrite -o BOOK.PDF BOOK.PS`

The output file is `BOOK.PDF`.

The cover is a separate PDF, generated like so:

`C:\ghostscript\gs10.04.0\bin\gswin64c.exe -dNOSAFER -sDEVICE=pdfwrite -o BOOK-COVER.PDF BOOK-COVER.PS`

# PostScript 
The PostScript programming language is used to here to make a pleasing PDF output, suitable for printing.
(I'm using lulu.com for printing the book.)
The top-level PostScript file is `BOOK.PS`. 
It uses the other PostScript files in this project, which form a simple library of commonly used things in PostScript programs.

Years ago, I used the *iText* Java library for creating PDF files programmatically.
I now much prefer using the PostScript language for such tasks. 