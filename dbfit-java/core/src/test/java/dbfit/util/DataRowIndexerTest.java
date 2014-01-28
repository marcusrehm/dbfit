package dbfit.util;

import dbfit.util.DataRowIndexer;
import dbfit.util.MatchableDataTable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataRowIndexerTest {

    private dbfit.util.RowStructure rowStructure = new RowStructure(
            new String[] { "n", "2n", "3n", "4n", "5n" }, /* names */
            new boolean[] { true, true, true, false, false } /* keys */
        );

    @Test
    @SuppressWarnings("unchecked")
    public void testRowKeyEquals() {
        DataRowKey rowKey1 = new DataRowKey(rowStructure, createRow(1, 2, 3, 4, 5));
        DataRowKey rowKey2 = new DataRowKey(rowStructure, createRow(1, 2, 3, 4, 5));

        assertEquals(rowKey1, rowKey2);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRowKeyAreNotEqualsWithDifferentValues() {
        DataRowKey rowKey1 = new DataRowKey(rowStructure, createRow(1, 6, 3, 4, 5));
        DataRowKey rowKey2 = new DataRowKey(rowStructure, createRow(1, 2, 3, 4, 5));

        Assert.assertFalse(rowKey1.equals(rowKey2));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRowKeyAreNotEqualsWithSameValuesInDifferentKeys() {
        DataRowKey rowKey1 = new DataRowKey(rowStructure, createRow(1, 2, 3, 4, 5));
        DataRowKey rowKey2 = new DataRowKey(rowStructure, createRow(3, 1, 2, 4, 5));

        Assert.assertFalse(rowKey1.equals(rowKey2));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMatchingRows() throws NoMatchingRowFoundException {
        LinkedList<DataRow> rows = new LinkedList<DataRow>();
        rows.add(createRow(1, 2, 3, 4, 5));
        rows.add(createRow(2, 4, 6, 8, 10));
        rows.add(createRow(3, 6, 9, 12, 15));
        MatchableDataTable mdt = createMatchableDataTable(rows);

        DataRow rowToSearch = createRow(1, 2, 3, 4, 5);
        DataRow rowFound = null;

        DataRowIndexer indexer = new DataRowIndexer(rowStructure, mdt);
        rowFound = indexer.findMatching(rowToSearch);

        for(String keyName : rowToSearch.getColumnNames()) {
            Assert.assertEquals(rowToSearch.get(keyName), rowFound.get(keyName));
        }
    }

    @Test(expected = NoMatchingRowFoundException.class) 
    @SuppressWarnings("unchecked")
    public void testMissingRows() throws NoMatchingRowFoundException {
        
        LinkedList<DataRow> rows = new LinkedList<DataRow>();
        rows.add(createRow(1, 2, 3, 4, 5));
        rows.add(createRow(2, 4, 6, 8, 10));
        rows.add(createRow(3, 6, 9, 12, 15));
        MatchableDataTable mdt = createMatchableDataTable(rows);

        DataRow rowToSearch = createRow(1, 7, 3, 4, 5);
        DataRow rowFound = null;

        DataRowIndexer indexer = new DataRowIndexer(rowStructure, mdt);
        rowFound = indexer.findMatching(rowToSearch);
        Assert.fail();
    }

    private DataRow createRow(int... items) {
        HashMap<String, Object> rowValues = new HashMap<String, Object>();
        int i = 0;
        for (Integer item: items) {
            rowValues.put(rowStructure.getColumnName(i++), item.toString());
        }
        return new DataRow(rowValues);
    }

    private MatchableDataTable createMatchableDataTable(List<DataRow> rows) {
        return new MatchableDataTable(new DataTable(rows, createColumns()));
    }

    private List<DataColumn> createColumns() {
        List<DataColumn> columns = new LinkedList<DataColumn>();
        for (String s: rowStructure.getColumnNames()) {
            columns.add(new DataColumn(s, s.getClass().getName(), ""));
        }
        return columns;
    }
}
