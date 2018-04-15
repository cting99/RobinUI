package cting.com.robineeee.support.common.model;

import java.util.ArrayList;

public interface IDataImportExport<I> {
    void exportData(ArrayList<I> items);
    void importData();
}
