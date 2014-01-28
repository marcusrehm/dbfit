package dbfit.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.HashMap;

public class DataRowIndexer {
    private RowStructure rowStructure;
    private MatchableDataTable dt;
    private HashMap<DataRowKey, DataRow> index;

    public DataRowIndexer(RowStructure rowStructure, MatchableDataTable dt) {
        this.index = new HashMap<DataRowKey, DataRow>(rowStructure.getKeyPropertiesNames().length);
        this.rowStructure = rowStructure;
        this.dt = dt;
        buildIndex();
    }

    private void buildIndex() {
        for (DataRow row : dt.getValue().getRows()) {
            index.put(new DataRowKey(rowStructure, row), row);
        }
    }

    public DataRow findMatching(DataRow row) throws NoMatchingRowFoundException {
        DataRow matchingRow = index.get(new DataRowKey(rowStructure, row));
        if(matchingRow != null) {
            return matchingRow;
        }

        throw new NoMatchingRowFoundException();
    }
}

