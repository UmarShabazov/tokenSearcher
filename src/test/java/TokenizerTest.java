import org.example.tokenization.WordByWordTokenizer;
import org.junit.jupiter.api.Test;
import org.example.tokenization.Tokenizer;
import org.example.tokenization.TrigramTokenizer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


public class TokenizerTest {

    @Test
    public void testWordTokenizerWithNormalText() {

        Tokenizer tokenizer = new WordByWordTokenizer();

        String text = "This is a simple test";

        String[] expectedTokens = {"this", "is", "a", "simple", "test"};

        assertArrayEquals(expectedTokens, tokenizer.tokenize(text), "Токены должны совпадать с ожидаемыми");
    }

    @Test
    public void testTrigramTokenizerWithNormalText() {
        Tokenizer tokenizer = new TrigramTokenizer();

        String text = "tokenization";

        String[] expectedTrigrams = {"tok", "oke", "ken", "eni", "niz", "iza", "zat", "ati", "tio", "ion"};

        assertArrayEquals(expectedTrigrams, tokenizer.tokenize(text), "Trigrams should coincide with the expected");
    }

    @Test
    public void testWordTokenizerWithEmptyText() {

        Tokenizer tokenizer = new WordByWordTokenizer();

        String text = "";

        String[] expectedTokens = {};

        assertArrayEquals(expectedTokens, tokenizer.tokenize(text), "An empty string should return an empty array of tokens");
    }

    @Test
    public void testTrigramTokenizerWithEmptyText() {

        Tokenizer tokenizer = new TrigramTokenizer();

        String text = "";

        String[] expectedTrigrams = {};

        assertArrayEquals(expectedTrigrams, tokenizer.tokenize(text), "An empty string should return an empty array of trigrams");
    }

    @Test
    public void testTrigramTokenizerWithShortText() {

        Tokenizer tokenizer = new TrigramTokenizer();

        String text = "ab";

        String[] expectedTrigrams = {};

        assertArrayEquals(expectedTrigrams, tokenizer.tokenize(text), "A string less than 3 characters long should return an empty array");
    }
}
