package dbfit.util;

import dbfit.util.DataRow;
import dbfit.util.RowStructure;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DataRowKey {
    private HashMap<String, Object> properties;
    private RowStructure rowStructure;

    public DataRowKey(RowStructure rowStructure, DataRow row) {
        this.rowStructure = rowStructure;
        properties = new HashMap<String, Object>();
        for (String property : rowStructure.getKeyPropertiesNames()) {
            properties.put(property, row.getStringValue(property));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        DataRowKey rowKeyToCompare = (DataRowKey) obj;
        EqualsBuilder builder = new EqualsBuilder();
        for (String keyName : rowStructure.getKeyPropertiesNames()) {
            builder.append(this.properties.get(keyName), rowKeyToCompare.properties.get(keyName));
        }
        
        return builder.isEquals();
    }

    
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        for (Map.Entry<String, Object> set : this.properties.entrySet()) {
            builder.append(set);
        }
        
        return builder.toHashCode();
    }
}
