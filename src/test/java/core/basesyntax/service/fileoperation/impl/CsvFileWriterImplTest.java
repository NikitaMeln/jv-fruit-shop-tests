package core.basesyntax.service.fileoperation.impl;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.model.Fruit;
import core.basesyntax.service.fileoperation.CreateReport;
import core.basesyntax.service.fileoperation.CsvFileWriter;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.Assert.*;

public class CsvFileWriterImplTest {
    private static final String OUTPUT_SOURCE = "src/test/resources/test-output.csv";
    private final StorageDao dao = new StorageDaoImpl();
    private final CreateReport report = new CreateReportImpl(new StringBuilder(), dao);
    private final CsvFileWriter writer = new CsvFileWriterImpl();

    @Before
     public void setDao() {
        dao.addFruit(new Fruit("apple", 40));
        dao.addFruit(new Fruit("peach", 50));
        dao.addFruit(new Fruit("apricot", 10));
    }

    @Test
    public void write_report_Ok() {
        writer.writeFile(OUTPUT_SOURCE, report.getReport());
        List<String> actual = new CsvFileReaderImpl().inputFile(OUTPUT_SOURCE);
        List<String> expected = dao.getAll()
                .stream()
                .map(s ->s.getName() + "," + s.getAmountFruit())
                .collect(Collectors.toList());
        String TITLE = "fruit,quantity";
        expected.add(0, TITLE);
        assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void write_invalidPath_notOk(){
        writer.writeFile("",report.getReport());
    }

}