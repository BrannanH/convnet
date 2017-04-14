package fundamentals;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

public class HelperLibrary {
	
	public static List<Integer> arrayAsList(int[] t) {
		return stream(t).boxed().collect(toList());
	}
	
	public static <T> List<T> cloneList(List<T> t) {
		List<T> r = new ArrayList<>();
		r.addAll(t);
		return r;
	}
	
//	public static int[] listAsArray(List<Integer> t) {
//		int[] r = new int[t.size()];
//		for(int i = 0; i < t.size(); i++) {
//			r[i] = t.get(i).intValue();
//		}
//		return r;
//	}
}
