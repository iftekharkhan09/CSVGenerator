import net.andreinc.mockneat.MockNeat;
import net.andreinc.mockneat.abstraction.MockUnitLong;
import net.andreinc.mockneat.unit.seq.LongSeq;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static net.andreinc.mockneat.unit.text.Formatter.fmt;

public class CSVGenerator {
    public static void main(String[] args) {
        MockNeat m = MockNeat.threadLocal ();
        final Path path = Paths.get ("./test.csv");

        MockUnitLong itemCodeSequenceGenerator = new LongSeq (470000L, 1);
        MockUnitLong subCategoryCode = new LongSeq (10, 1);
        MockUnitLong priceLineCode = new LongSeq (10000L, 1);

        m.fmt ("#{ItemCode}|#{ItemName}|#{SubCategoryCode}|#{PriceLineCode}|#{IsPrivate}")
                .param ("ItemCode", itemCodeSequenceGenerator)
                .param ("ItemName",  fmt("#{Product}#{num}")
                        .param("Product", "Product")
                        .param("num", new LongSeq (10, 1)))
                .param ("SubCategoryCode", subCategoryCode)
                .param ("PriceLineCode", priceLineCode)
                .param ("IsPrivate", m.bools ())
                .list (1000000)
                .consume (list -> {
                    try {
                        Files.write (path, list, CREATE, WRITE);
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }
                });
    }
}
