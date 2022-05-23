package kernel;
import java.util.Comparator;
import java.util.List;

public class ItemSorterByValueAndAbsoluteRC implements ItemSorter
{
	private Comparator<Item> comparator =
		(e1, e2) -> {
			var valE1= e1.getXr();
			var valE2 =e2.getXr();
			if(valE1 < valE2)
				return 1;
			else if(valE1 > valE2)
				return -1;
			else if(valE1 == 1.0)
				return 0;
//			else if(Math.abs(valE1) < 1e-5 && Math.abs(valE2) < 1e-5) {
			else if((Math.abs(valE1) == Math.abs(valE2))) {
				if(Math.abs(e1.getRc()) > Math.abs(e2.getRc()))
					return 1;
				else if (Math.abs(e1.getRc()) < Math.abs(e2.getRc()))
					return -1;
			}
			return 0;
		};
	@Override
	public void sort(List<Item> items) {
		items.sort(this.comparator);
	}

}
