# tokenSearch
# Text File Indexing and Search System

This project is a console application for indexing text files and performing keyword searches. Its flexible architecture allows it to be easily adapted for various requirements and tokenization methods.

## Overview

The application allows users to:

- Index text files located in specified directories.
- Search for files containing specified words or phrases.
- Support multiple user interaction modes (command line or console interface).

## Key Components

### 1. `ApplicationMode`
The `ApplicationMode` interface defines different modes of application operation:

- CommandLineService — a command-line implementation that allows file paths and search queries to be passed as arguments.
- ConsoleService — an interactive console implementation that enables the user to manually enter commands.

### 2. `Search Engine Interface (SearchEngine)`
The `SearchEngine` interface manages core indexing and search operations:

- addPath(String path) — adds the specified path (file or directory) to the index.
- search(String query) — performs a keyword search.
- printIndex() — outputs the current index state.

Implementation: IndexService — an implementation of Indexable that processes tokens and files, using Indexer for index storage.

### 3. `Token Indexer Interface (TokenIndexer)`
`TokenIndexer` interface is responsible for managing the internal index structure and associating tokens with files:

-addToken(String token, File file) — associates a file with a specified token.
-getFilesForToken(String token) — retrieves files associated with a particular token.
-printIndex() — outputs the current index.

Implementation: InMemoryIndexer — stores the index in memory using a Map<String, Set<File>> structure. Supports adding tokens and outputting the current index state.

### 4. `Tokenizer (Tokenization Interface)`
The `Tokenizer` interface defines methods for tokenizing file text. Supporting different tokenization methods allows the system to adapt to various data formats:

- tokenize(String content) — splits text into tokens.

Implementations:
- WordByWordTokenizer — splits text into individual words, using regular expressions to handle word boundaries.
- TrigramTokenizer — splits text into trigrams (groups of three characters), allowing for more precise substring searches.

### 5. `FileChecker Utility`
The `FileCheckerUtil` class is responsible for verifying if a file is a valid text file before indexing. It ensures the file has an appropriate extension and valid UTF-8 encoding, filtering out binary files and unsupported formats. 
