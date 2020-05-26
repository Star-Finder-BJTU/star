
public class random_assortment {
	int checkerCount = ds.getCount();//
	int curGroupingNum = Integer.parseInt(groupingNum);
	int remainderRes = checkerCount % curGroupingNum;
	int divisionRes = checkerCount / curGroupingNum;
	
	if (checkerCount > curGroupingNum) {
	    if (0 == remainderRes) {
			int tempDivisionRes = divisionRes;
			int groupNum = curGroupingNum;
			for (int i = 0; i < ds.getCount(); i++) {
				ds.getRecord(i).set("GROUPING_INFO","第 " + String.valueOf((curGroupingNum - groupNum) + 1) + " 组");
				ds.getRecord(i).set("GROUPING_VALUE", String.valueOf((curGroupingNum - groupNum) + 1));
				tempDivisionRes--;
				if (tempDivisionRes == 0) {
					tempDivisionRes = divisionRes;
					groupNum--;
				}
			}
		} 
		else {
			int tempDivisionRes = divisionRes;
			int groupNum = curGroupingNum;
			for (int i = 0; i < ds.getCount(); i++) {
				if (i < ds.getCount() - (remainderRes + divisionRes)) {
				ds.getRecord(i).set("GROUPING_INFO","第 " + String.valueOf((curGroupingNum - groupNum) + 1) + " 组");
				ds.getRecord(i).set("GROUPING_VALUE", String.valueOf((curGroupingNum - groupNum) + 1));
				tempDivisionRes--;
					if (tempDivisionRes == 0) {
						tempDivisionRes = divisionRes;
						groupNum--;
					}
					} 
					else {
						ds.getRecord(i).set("GROUPING_INFO","第 " + String.valueOf((curGroupingNum - groupNum) + 1) + " 组");
						ds.getRecord(i).set("GROUPING_VALUE", String.valueOf((curGroupingNum - groupNum) + 1));
					}
		    }
	
		}

	} 
	else {
		for (int i = 0; i < ds.getCount(); i++) {
			ds.getRecord(i).set("GROUPING_INFO", "第 1 组");
			ds.getRecord(i).set("GROUPING_VALUE", "1");
		}
	}
}
