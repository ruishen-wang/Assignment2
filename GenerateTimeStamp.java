
/* ass02
 * Wrote by Hongyi Yan
 * s3521449
 */

package application;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateTimeStamp {
	public String getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(currentTime);

	}
}
